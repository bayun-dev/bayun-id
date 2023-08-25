package dev.bayun.id.core.service;

import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.Person;
import dev.bayun.id.core.modal.AccountUpdateToken;
import dev.bayun.id.core.modal.AccountCreateToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

public interface AccountService extends UserDetailsService {

    @Deprecated(forRemoval = true)
    Account create(String username, Person person, String password, String email);

    Account create(AccountCreateToken token);

    Account delete(UUID id);

    boolean exists(UUID id);

    boolean exists(String username);

    Account loadUserById(UUID id);

    @Override
    Account loadUserByUsername(String username) throws UsernameNotFoundException;

    Account update(UUID id, AccountUpdateToken token);

}
