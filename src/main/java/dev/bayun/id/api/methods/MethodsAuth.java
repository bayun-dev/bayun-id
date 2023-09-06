package dev.bayun.id.api.methods;

import dev.bayun.id.api.schema.response.AbstractBaseResponse;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.Email;
import dev.bayun.id.core.exception.*;
import dev.bayun.id.core.entity.account.AccountCreateToken;
import dev.bayun.id.core.service.AccountService;
import dev.bayun.id.core.service.AuthenticationService;
import dev.bayun.id.core.service.email.EmailService;
import dev.bayun.id.core.validation.annotation.FirstName;
import dev.bayun.id.core.validation.annotation.LastName;
import dev.bayun.id.core.validation.annotation.Password;
import dev.bayun.id.core.validation.annotation.Username;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class MethodsAuth {

    @Setter
    private AuthenticationService authenticationService;

    @Setter
    private AccountService accountService;

    @Setter
    private EmailService emailService;

    @Setter
    private MethodHelper methodHelper;

    @Setter
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Setter
    private String defaultRedirectUrl = "/";

    public MethodsAuth(AuthenticationService authenticationService, AccountService accountService, EmailService emailService, MethodHelper methodHelper) {
        this.authenticationService = authenticationService;
        this.accountService = accountService;
        this.emailService = emailService;
        this.methodHelper = methodHelper;
    }

    @PostMapping(path = "/api/methods/auth.resetPassword",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthResetPasswordResponse resetPassword(@Valid AuthResetPasswordToken token, BindingResult bindingResult) throws MessagingException, BindException {
        methodHelper.checkBindingResult(bindingResult);

        try {
            Account account = accountService.loadUserByUsername(token.getUsername());
            Email email = emailService.loadById(account.getEmailId());
            if (email == null || !email.isConfirmed()) {
                throw new EmailNotConfirmedException("Unable to reset password because the email is not confirmed: %s".formatted(Objects.requireNonNullElse(email, "null")));
            }

            String rawPassword = accountService.resetPassword(account.getId());
            emailService.sendRawPassword(email, rawPassword);

            return new AuthResetPasswordResponse(emailService.toHidden(email.getEmail()));
        } catch (UsernameNotFoundException e) {
            throw new UsernameUnoccupiedException(token.getUsername(), e);
        }
    }

    @PostMapping(path = "/api/methods/auth.signIn",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthSignInResponse signIn(@Valid AuthSignInToken token, BindingResult bindingResult,
            HttpServletRequest request, HttpServletResponse response) throws BindException {
        methodHelper.checkBindingResult(bindingResult);

        try {
            authenticationService.authenticate(UsernamePasswordAuthenticationToken
                    .unauthenticated(token.getUsername(), token.getPassword()), request, response);
        } catch (UsernameNotFoundException e) {
            throw new UsernameUnoccupiedException(token.getUsername(), e);
        } catch (BadCredentialsException e) {
            throw new PasswordInvalidException("Unable to authenticate because the password is invalid", e);
        }

        return new AuthSignInResponse(retrieveRedirectUrl(request, response));
    }

    @PostMapping(path = "/api/methods/auth.signOut",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthSignOutResponse signOut(HttpServletRequest request, HttpServletResponse response) {
        authenticationService.signOut(request, response);

        return new AuthSignOutResponse();
    }

    @PostMapping(path = "/api/methods/auth.signUp",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthSignUpResponse signUp(@Valid AuthSingUpToken token, BindingResult bindingResult) throws BindException {
        methodHelper.checkBindingResult(bindingResult);

        try {
            accountService.create(token);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameOccupiedException(token.getUsername(), e);
        }

        return new AuthSignUpResponse();
    }

    private String retrieveRedirectUrl(HttpServletRequest request, HttpServletResponse response) {
        SavedRequest savedRequest = this.requestCache.getRequest(request, response);
        String redirectUrl = savedRequest == null ? this.defaultRedirectUrl : savedRequest.getRedirectUrl();
        this.requestCache.removeRequest(request, response);
        return redirectUrl;
    }

    @Getter
    public static class AuthResetPasswordResponse extends AbstractBaseResponse {

        private final String email;

        public AuthResetPasswordResponse(String email) {
            this.email = email;
        }
    }

    @Getter
    @Setter
    public static class AuthResetPasswordToken {

        @Username
        private String username;

    }

    @Getter
    public static class AuthSignInResponse extends AbstractBaseResponse {

        private final String redirectUri;

        public AuthSignInResponse(String redirectUri) {
            this.redirectUri = redirectUri;
        }
    }

    @Getter
    @Setter
    public static class AuthSignInToken {
        @Username
        private String username;

        @Password
        private String password;
    }

    public static class AuthSignOutResponse extends AbstractBaseResponse { }

    public static class AuthSignUpResponse extends AbstractBaseResponse { }

    public static class AuthSingUpToken extends AccountCreateToken {

        @Override
        @Username
        public String getUsername() {
            return super.getUsername();
        }

        @Override
        @FirstName
        public String getFirstName() {
            return super.getFirstName();
        }

        @Override
        @LastName
        public String getLastName() {
            return super.getLastName();
        }

        @Override
        @Password
        public String getPassword() {
            return super.getPassword();
        }
    }

}
