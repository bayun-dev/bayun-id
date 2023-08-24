package dev.bayun.id.core.service;

import dev.bayun.id.core.entity.account.*;
import dev.bayun.id.core.exception.AccountNotFoundException;
import dev.bayun.id.core.exception.AccountRegistrationException;
import dev.bayun.id.core.repository.AccountRepository;
import dev.bayun.id.core.util.converter.UUIDGenerator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DefaultAccountService implements AccountService {

    @Setter
    private AccountRepository accountRepository;

    @Setter
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public Account create(String username, Person person, String password, String email) {
        long currentTime = System.currentTimeMillis();

        try {
            Account registered = new Account();
            registered.setId(UUIDGenerator.getV7());
            registered.setUsername(username);
            registered.setPerson(person);

            Contact registeredContact = new Contact();
            registeredContact.setEmail(email);
            registeredContact.setEmailConfirmed(false);
            registered.setContact(registeredContact);

            Secret registeredSecret = new Secret();
            registeredSecret.setHash(passwordEncoder.encode(password));
            registeredSecret.setLastModifiedDate(currentTime);
            registered.setSecret(registeredSecret);

            Details details = new Details();
            details.setRegistrationDate(currentTime);
            registered.setDetails(details);

            Deactivation deactivation = new Deactivation();
            deactivation.setDeactivated(false);
            registered.setDeactivation(deactivation);

            Set<Authority> registeredAuthorities = new HashSet<>();
            registeredAuthorities.add(Authority.ROLE_USER);
            registered.setAuthorities(registeredAuthorities);

            return accountRepository.save(registered);
        } catch (Exception e) {
            throw new AccountRegistrationException(e);
        }
    }

    @Override
    public boolean exists(UUID id) {
        return accountRepository.existsById(id);
    }

    @Override
    public boolean exists(String username) {
        return accountRepository.existsByUsername(username);
    }

    @Override
    public Account loadUserById(UUID id) {
        Assert.notNull(id, "The id must not be null");
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "account with provided id (%s) not found".formatted(id.toString())));
    }

    @Override
    public Account loadUserByUsername(String username) {
        Assert.notNull(username, "The username must not be null");
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "account with provided username (%s) not found".formatted(username)));
    }
}
