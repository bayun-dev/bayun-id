package dev.bayun.id.core.service;

import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.Person;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.UUID;

public interface AccountService extends UserDetailsService {

    Account create(String username, Person person, String password, String email);

    boolean exists(UUID id);

    boolean exists(String username);

    Account loadUserById(UUID id);

    @Override
    Account loadUserByUsername(String username) throws UsernameNotFoundException;

}
