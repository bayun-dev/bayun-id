package dev.bayun.id.api.endpoint.accounts;

import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.api.schema.response.DeleteAccountsByIdResponse;
import dev.bayun.id.api.schema.response.ErrorResponse;
import dev.bayun.id.api.schema.response.GetAccountsByIdResponse;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.exception.AccountNotFoundException;
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
public class DeleteAccountsById {

    private AccountService accountService;

    private MethodsAccountsByIdHelper accountsByIdHelper;

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(
            path = "/api/accounts/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public DeleteAccountsByIdResponse handle(@PathVariable String id, Authentication authentication) {
        Account account = accountsByIdHelper.getAccountByPathVariableId(id, authentication);
        accountService.delete(account.getId());

        return new DeleteAccountsByIdResponse();
    }
}
