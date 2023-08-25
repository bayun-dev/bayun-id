package dev.bayun.id.api.endpoint.accounts;

import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.api.schema.response.ErrorResponse;
import dev.bayun.id.api.schema.response.GetAccountsByIdResponse;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.exception.AccountNotFoundException;
import dev.bayun.id.core.exception.AccountRegistrationException;
import dev.bayun.id.core.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class GetAccountsById {

    private AccountService accountService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(
            path = "/api/accounts/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public GetAccountsByIdResponse handle(@PathVariable String id, Authentication authentication) {
        if (id == null) {
            throw new AccountNotFoundException("the provided account id is null");
        }

        Account account;
        try {
            if (id.equalsIgnoreCase("me")) {
                String username = (String) authentication.getPrincipal();
                account = accountService.loadUserByUsername(username);
            } else {
                account = accountService.loadUserById(UUID.fromString(id));
            }
        } catch (AccountNotFoundException | UsernameNotFoundException | IllegalArgumentException exception) {
            throw new AccountRegistrationException(Errors.ACCOUNT_NOT_FOUND_CODE);
        }

        return new GetAccountsByIdResponse(account);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> accountNotFoundExceptionHandle() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(Errors.ACCOUNT_NOT_FOUND));
    }
}
