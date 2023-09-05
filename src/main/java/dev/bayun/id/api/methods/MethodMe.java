package dev.bayun.id.api.methods;

import dev.bayun.id.api.schema.response.AbstractBaseResponse;
import dev.bayun.id.core.entity.SessionToken;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.AccountUpdateToken;
import dev.bayun.id.core.entity.account.Avatar;
import dev.bayun.id.core.entity.account.Email;
import dev.bayun.id.core.exception.InternalErrorException;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.Session;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
public class MethodMe {

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

    @PostMapping(path = "/api/methods/me.delete")
    public MeDeleteResponse delete(@Valid MeDeleteToken token, BindingResult bindingResult, Authentication authentication) {
        methodHelper.checkBindingResult(bindingResult);

        Account me = getMeByAuthentication(authentication);

        if (passwordEncoder.matches(token.password, me.getPasswordHash())) {
            accountService.delete(me.getId());
        } else {
            throw new PasswordInvalidException();
        }

        return new MeDeleteResponse();
    }

    /**
     * Получить основную информацию о личном аккаунте.
     */
    @GetMapping(path = "/api/methods/me.get",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public MeGetResponse get(Authentication authentication) {
        Account me = getMeByAuthentication(authentication);
        Email email = emailService.loadById(me.getEmailId());

        return MeGetResponse.builder()
                .id(me.getId().toString())
                .avatarId(me.getAvatarId())
                .firstName(me.getFirstName())
                .email(email == null ? null : emailService.toHidden(email.getEmail()))
                .emailConfirmed(email == null ? null : email.isConfirmed())
                .lastName(me.getLastName())
                .username(me.getUsername())
                .build();
    }

    @GetMapping(path = "/api/methods/me.getActivityHistory",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public MeGetActivityHistoryResponse getActivityHistory(Authentication authentication) {
        Account me = getMeByAuthentication(authentication);

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
    @GetMapping(path = "/api/methods/me.emailConfirm")
    public ResponseEntity<?> emailConfirm(@RequestParam("hash") String emailIdStr, Authentication authentication) {
        Account me = getMeByAuthentication(authentication);
        UUID emailId;
        try {
            emailId = UUID.fromString(emailIdStr);
        } catch (Exception e) {
            throw new InternalErrorException(new Exception("The provided email not in format", e));
        }

        if (me.getEmailId() == null || !me.getEmailId().equals(emailId)) {
            throw new InternalErrorException(new Exception("Can't email confirm: meEmailId=%s, provided emailId=%s"
                    .formatted(me.getEmailId(), emailId)));
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
    @PostMapping(path = "/api/methods/me.save",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public MeSaveResponse save(@Valid MeSaveToken token, BindingResult bindingResult,
                               Authentication authentication) throws IOException {
        methodHelper.checkBindingResult(bindingResult);

        Account me = getMeByAuthentication(authentication);

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
                    throw new InternalErrorException(e);
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

    public Account getMeByAuthentication(Authentication authentication) {
        if (authentication != null) {
            return accountService.loadUserByUsername(authentication.getName());
        } else {
            return null;
        }
    }

    public static class MeDeleteResponse extends AbstractBaseResponse {

    }

    @Data
    public static class MeDeleteToken {

        @Password
        private String password;

    }

    @Getter
    @Builder(builderClassName = "Builder")
    public static class MeGetResponse extends AbstractBaseResponse {

        /**
         * Account id
         */
        private String id;

        /**
         * Account avatar id,
         * null if default avatar
         */
        private String avatarId;

        /**
         * Account email in a secure format, for example: ma***@example.com,
         * null if no email
         */
        private String email;

        /**
         * Is account email confirmed,
         * true if email confirmed,
         * false if email not confirmed,
         * null if email is null
         */
        private Boolean emailConfirmed;

        /**
         * First name
         */
        private String firstName;

        /**
         * Last name
         */
        private String lastName;

        /**
         * Username
         */
        private String username;

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
