package dev.bayun.id.api.endpoint.accounts;

import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.exception.AccountNotFoundException;
import dev.bayun.id.core.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Service
@AllArgsConstructor
public class MethodsAccountsByIdHelper {

    private AccountService accountService;

    public Account getAccountByPathVariableId(String id, Authentication authentication) {
        try {
            if (id.equalsIgnoreCase("me")) {
                String username = authentication.getName();
                return accountService.loadUserByUsername(username);
            } else {
                return accountService.loadUserById(UUID.fromString(id));
            }
        } catch (AccountNotFoundException | UsernameNotFoundException | IllegalArgumentException exception) {
            throw new AccountNotFoundException();
        }
    }
}
