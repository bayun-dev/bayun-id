package dev.bayun.id.core.service;

import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.AccountCreateToken;
import dev.bayun.id.core.entity.account.AccountUpdateToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

public interface AccountService extends UserDetailsService {

    Account block(UUID id);

    Account create(AccountCreateToken token);

    Account delete(UUID id);

    boolean exists(UUID id);

    boolean exists(String username);

    Account loadUserById(UUID id);

    @Override
    Account loadUserByUsername(String username) throws UsernameNotFoundException;

    String resetPassword(UUID id);

    Account update(UUID id, AccountUpdateToken token);

}
