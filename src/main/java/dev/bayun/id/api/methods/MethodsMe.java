package dev.bayun.id.api.methods;

import dev.bayun.id.api.schema.response.AbstractBaseResponse;
import dev.bayun.id.core.entity.SessionToken;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.AccountUpdateToken;
import dev.bayun.id.core.entity.account.Avatar;
import dev.bayun.id.core.entity.account.Email;
import dev.bayun.id.core.exception.PasswordInvalidException;
import dev.bayun.id.core.secure.annotation.IsUser;
import dev.bayun.id.core.service.AccountService;
import dev.bayun.id.core.service.ActivityHistoryService;
import dev.bayun.id.core.service.AvatarService;
import dev.bayun.id.core.service.email.EmailService;
import dev.bayun.id.core.validation.annotation.FirstName;
import dev.bayun.id.core.validation.annotation.LastName;
import dev.bayun.id.core.validation.annotation.Password;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.Session;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@IsUser
@RestController
@AllArgsConstructor
public class MethodsMe {

    public static final String URI_ME_GET_VALUE = "/api/methods/me.get";
    public static final String URI_ME_SAVE_VALUE = "/api/methods/me.save";
    public static final String URI_ME_DELETE_VALUE = "/api/methods/me.delete";
    public static final String URI_ME_EMAIL_CONFIRM_VALUE = "/api/methods/me.emailConfirm";

    @Setter
    private AccountService accountService;

    @Setter
    private ActivityHistoryService activityHistoryService;

    @Setter
    private PasswordEncoder passwordEncoder;

    @Setter
    private AvatarService avatarService;

    @Setter
    private EmailService emailService;

    @Setter
    private MethodHelper methodHelper;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = MethodsMe.URI_ME_DELETE_VALUE)
    public MeDeleteResponse delete(@Valid MeDeleteToken token, BindingResult bindingResult, Authentication authentication) throws BindException {
        methodHelper.checkBindingResult(bindingResult);

        Account me = accountService.loadUserByUsername(authentication.getName());

        if (passwordEncoder.matches(token.password, me.getPasswordHash())) {
            accountService.delete(me.getId());
        } else {
            throw new PasswordInvalidException("The account cannot be deleted because the password is invalid");
        }

        return new MeDeleteResponse();
    }
    
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = MethodsMe.URI_ME_GET_VALUE)
    public MeGetResponse get(Authentication authentication) {
        Account me = accountService.loadUserByUsername(authentication.getName());
        Email email = emailService.loadById(me.getEmailId());

        return new MeGetResponse(me,
                email == null ? null : emailService.toHidden(email.getEmail()),
                email == null ? null : email.isConfirmed());
    }

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST},
            path = "/api/methods/me.getActivityHistory"
    )
    public MeGetActivityHistoryResponse getActivityHistory(Authentication authentication) {
        Account me = accountService.loadUserByUsername(authentication.getName());

        Collection<? extends Session> sessions = activityHistoryService.getByPrincipalName(me.getUsername());

        // ip
        // last active time
        // completed

        return new MeGetActivityHistoryResponse(sessions.stream()
                .map(session -> new SessionToken("localhost", session.getLastAccessedTime().toEpochMilli(),session.isExpired()))
                .collect(Collectors.toList()));
    }

    /**
     * Подтвердить почтовый адрес личного аккаунта,
     * Если успешно, вернет редирект на страницу аккаунта,
     * иначе вернет страницу с ошибкой 500 INTERNAL_ERROR.
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = MethodsMe.URI_ME_EMAIL_CONFIRM_VALUE)
    public ResponseEntity<?> emailConfirm(@RequestParam(value = "hash", required = false) String emailIdStr, Authentication authentication) {
        Account me = accountService.loadUserByUsername(authentication.getName());
        UUID emailId;
        try {
            emailId = UUID.fromString(emailIdStr);
        } catch (Exception e) {
            throw new RuntimeException("The provided email not in format", e);
        }

        if (me.getEmailId() == null || !me.getEmailId().equals(emailId)) {
            throw new RuntimeException("Can't email confirm: meEmailId=%s, provided emailId=%s"
                    .formatted(me.getEmailId(), emailId));
        }

        emailService.confirm(emailId);

        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/")).build();
    }

    /**
     * Изменить информацию о личном аккаунте.
     * Возможные ошибки:
     * * BAD_REQUEST
     * * BAD_REQUEST_PARAMETERS
     */
    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST}, path = MethodsMe.URI_ME_SAVE_VALUE)
    public MeSaveResponse save(@Valid MeSaveToken token, BindingResult bindingResult,
                               Authentication authentication) throws IOException, BindException {
        methodHelper.checkBindingResult(bindingResult);

        Account me = accountService.loadUserByUsername(authentication.getName());

        AccountUpdateToken.AccountUpdateTokenBuilder accountUpdateTokenBuilder = AccountUpdateToken.builder()
                .firstName(token.getFirstName())
                .lastName(token.getLastName())
                .dropAvatar(token.isDropAvatar())
                .password(token.getPassword());

        if (token.getEmail() != null) {
            Email meEmail = emailService.loadById(me.getEmailId());
            if (!(meEmail != null && meEmail.isConfirmed() && meEmail.getEmail().equals(token.getEmail()))) {
                Email email = emailService.save(token.getEmail());
                try {
                    emailService.sendConfirm(email);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                accountUpdateTokenBuilder.emailId(email.getId());
            }
        }

        if (!token.isDropAvatar() && token.getAvatar() != null) {
            Avatar avatar = avatarService.save(token.getAvatar().getBytes());
            accountUpdateTokenBuilder.avatarId(avatar.getId());
        }

        accountService.update(me.getId(), accountUpdateTokenBuilder.build());

        return new MeSaveResponse();
    }

    public static class MeDeleteResponse extends AbstractBaseResponse {

    }

    @Data
    public static class MeDeleteToken {

        @Password
        private String password;

    }

    @Getter
    @Data
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    public static class MeGetResponse extends AbstractBaseResponse {

        private String id;

        private String avatarId;

        private String email;

        private Boolean emailConfirmed;

        private String firstName;

        private String lastName;

        private String username;

        public MeGetResponse(Account account, String email, Boolean emailConfirmed) {
            this.id = account.getId().toString();
            this.avatarId = account.getAvatarId();
            this.firstName = account.getFirstName();
            this.email = email;
            this.emailConfirmed = emailConfirmed;
            this.lastName = account.getLastName();
            this.username = account.getUsername();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class MeGetActivityHistoryResponse extends AbstractBaseResponse {

        private List<SessionToken> history;

    }

    public static class MeSaveResponse extends AbstractBaseResponse { }

    @Data
    public static class MeSaveToken {

        @FirstName(required = false)
        private String firstName;

        @LastName(required = false)
        private String lastName;

        @Password(required = false)
        private String password;

        @dev.bayun.id.core.validation.annotation.Email(required = false)
        private String email;

        private MultipartFile avatar;

        private boolean dropAvatar;

    }
}
