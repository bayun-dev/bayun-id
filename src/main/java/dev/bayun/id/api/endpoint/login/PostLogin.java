package dev.bayun.id.api.endpoint.login;

import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.api.schema.request.PostLoginRequest;
import dev.bayun.id.api.schema.response.ErrorResponse;
import dev.bayun.id.api.schema.response.PostLoginResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PostLogin {

    @Setter
    private AuthenticationManager authenticationManager;

    @Setter
    private String defaultRedirectUrl = "/";

    @Setter
    private RequestCache requestCache = new HttpSessionRequestCache();

    @Setter
    private SecurityContextRepository securityContextRepository;

    public PostLogin(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.securityContextRepository = securityContextRepository;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(
            path = "/api/login",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PostLoginResponse handle(@Validated PostLoginRequest body, BindingResult bindingResult,
                HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Authentication token = UsernamePasswordAuthenticationToken.unauthenticated(body.getUsername(), body.getPassword());

        Authentication authResult = authenticationManager.authenticate(token);
        if (authResult == null) {
            throw new RuntimeException("authResult is null");
        }

        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authResult);

        SecurityContextHolder.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, httpServletRequest, httpServletResponse);

        SavedRequest savedRequest = this.requestCache.getRequest(httpServletRequest, httpServletResponse);
        String redirectUrl = savedRequest == null ? this.defaultRedirectUrl : savedRequest.getRedirectUrl();
        this.requestCache.removeRequest(httpServletRequest, httpServletResponse);
        return new PostLoginResponse(redirectUrl);
    }

    @ExceptionHandler(AuthenticationException.class)
    public void authenticationExceptionHandle(AuthenticationException exception) {
        throw new RuntimeException(exception);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(BadCredentialsException.class)
    public ErrorResponse badCredentialsExceptionHandle(BadCredentialsException exception) {
        return new ErrorResponse(Errors.CREDENTIALS_NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.LOCKED)
    @ExceptionHandler(AccountStatusException.class)
    public ErrorResponse accountStatusExceptionHandle(AccountStatusException exception) {
        if (exception instanceof LockedException) {
            return new ErrorResponse(Errors.ACCOUNT_BLOCKED);
        } else if (exception instanceof DisabledException) {
            return new ErrorResponse(Errors.ACCOUNT_DELETED);
        } else {
            throw new RuntimeException(exception);
        }
    }
}
