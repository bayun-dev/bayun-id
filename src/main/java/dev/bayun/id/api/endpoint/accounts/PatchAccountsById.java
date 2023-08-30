package dev.bayun.id.api.endpoint.accounts;

import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.api.schema.request.PatchAccountsByIdRequest;
import dev.bayun.id.api.schema.response.ErrorResponse;
import dev.bayun.id.api.schema.response.GetAccountsByIdResponse;
import dev.bayun.id.api.schema.response.PatchAccountsByIdResponse;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.exception.AccountNotFoundException;
import dev.bayun.id.core.exception.AccountUpdateException;
import dev.bayun.id.core.service.AccountService;
import dev.bayun.id.core.validation.annotation.Email;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class PatchAccountsById {

    private AccountService accountService;

    private MethodsAccountsByIdHelper accountsByIdHelper;

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(
            path = "/api/accounts/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PatchAccountsByIdResponse handle(@PathVariable String id, Authentication authentication,
                        @Valid PatchAccountsByIdRequest body, BindingResult bindingResult) throws BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        Account account = accountsByIdHelper.getAccountByPathVariableId(id, authentication);

        try {
            accountService.update(account.getId(), body);
        } catch (AccountUpdateException e) {
            return new PatchAccountsByIdResponse(false);
        }

        return new PatchAccountsByIdResponse(true);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MultipartException.class)
    public ErrorResponse multipartExceptionHandle(MultipartException e) {
        return new ErrorResponse(Errors.AVATAR_INVALID);
    }
}
