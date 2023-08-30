package dev.bayun.id.api.endpoint.accounts;

import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.api.schema.response.ErrorResponse;
import dev.bayun.id.api.schema.response.PostAccountsByIdAvatarResponse;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.Avatar;
import dev.bayun.id.core.exception.AccountNotFoundException;
import dev.bayun.id.core.modal.AccountUpdateToken;
import dev.bayun.id.core.service.AccountService;
import dev.bayun.id.core.service.AvatarService;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class DeleteAccountsByIdAvatar {

    @Setter
    private AccountService accountService;

    private MethodsAccountsByIdHelper accountsByIdHelper;

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(path = "/api/accounts/{id}/avatar")
    public void handle(@PathVariable("id") String accountId, Authentication authentication)
            throws IOException {

        Account account = accountsByIdHelper.getAccountByPathVariableId(accountId, authentication);
        accountService.setDefaultAvatar(account.getId());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultipartException.class)
    public ErrorResponse multipartExceptionHandle(MultipartException e) {
        return new ErrorResponse(Errors.AVATAR_INVALID);
    }
}
