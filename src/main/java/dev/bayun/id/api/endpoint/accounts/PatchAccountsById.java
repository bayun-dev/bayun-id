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

import java.util.UUID;

@RestController
@AllArgsConstructor
public class PatchAccountsById {

    private AccountService accountService;

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
        if (id == null) {
            throw new AccountNotFoundException("the provided account id is null");
        }

        Account account;
        try {
            if (id.equalsIgnoreCase("me")) {
                String username = authentication.getName();
                account = accountService.loadUserByUsername(username);
            } else {
                account = accountService.loadUserById(UUID.fromString(id));
            }
        } catch (AccountNotFoundException | UsernameNotFoundException | IllegalArgumentException exception) {
            throw new AccountNotFoundException(Errors.ACCOUNT_NOT_FOUND_CODE);
        }

        try {
            accountService.update(account.getId(), body);
        } catch (AccountUpdateException e) {
            return new PatchAccountsByIdResponse(false);
        }

        return new PatchAccountsByIdResponse(true);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> accountNotFoundExceptionHandle() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(Errors.ACCOUNT_NOT_FOUND));
    }
}
