package dev.bayun.id.core.service;

import dev.bayun.id.core.entity.account.*;
import dev.bayun.id.core.exception.AccountNotFoundException;
import dev.bayun.id.core.exception.AccountRegistrationException;
import dev.bayun.id.core.modal.AccountCreateToken;
import dev.bayun.id.core.modal.AccountUpdateToken;
import dev.bayun.id.core.repository.AccountRepository;
import dev.bayun.id.core.util.converter.UUIDGenerator;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class DefaultAccountService implements AccountService {

    @Setter
    private AccountRepository accountRepository;

    @Setter
    private PasswordEncoder passwordEncoder;

    @Override
    public Account create(AccountCreateToken token) {
        long currentTime = System.currentTimeMillis();

        try {
            Account account = new Account();
            account.setId(UUIDGenerator.getV7());
            account.setUsername(token.getUsername());

            Person person = new Person();
            person.setFirstName(token.getFirstName());
            person.setLastName(token.getLastName());
            person.setDateOfBirth(token.getDateOfBirth());
            person.setGender(Person.Gender.fromValue(token.getGender()));
            account.setPerson(person);

            Contact contact = new Contact();
            if (token.getEmail() != null) {
                contact.setEmail(token.getEmail());
                contact.setEmailConfirmed(false);
            }
            account.setContact(contact);

            Secret secret = new Secret();
            secret.setHash(passwordEncoder.encode(token.getPassword()));
            secret.setLastModifiedDate(currentTime);
            account.setSecret(secret);

            Details details = new Details();
            details.setRegistrationDate(currentTime);
            account.setDetails(details);

            Deactivation deactivation = new Deactivation();
            deactivation.setDeactivated(false);
            account.setDeactivation(deactivation);

            Set<Authority> authorities = new HashSet<>();
            authorities.add(Authority.ROLE_USER);
            account.setAuthorities(authorities);

            return accountRepository.save(account);
        } catch (Exception e) {
            throw new AccountRegistrationException(e);
        }
    }

    @Override
    public Account delete(UUID id) {
        Assert.notNull(id, "The id must not be null");

        Account account = loadUserById(id);

        Deactivation deactivation = account.getDeactivation();
        deactivation.setDeactivated(true);
        deactivation.setReason(Deactivation.Reason.DELETED);
        deactivation.setDate(System.currentTimeMillis());
        account.setDeactivation(deactivation);

        return accountRepository.save(account);
    }

    @Override
    public boolean exists(UUID id) {
        Assert.notNull(id, "The id must not be null");
        return accountRepository.existsById(id);
    }

    @Override
    public boolean exists(String username) {
        Assert.notNull(username, "The username must not be null");
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

    @Override
    public Account update(UUID id, AccountUpdateToken token) {
        Assert.notNull(id, "The id must not be null");
        Assert.notNull(id, "The token must not be null");

        Account account = loadUserById(id);

        Person person = account.getPerson();
        if (token.getFirstName() != null) {
            person.setFirstName(token.getFirstName());
        }
        if (token.getLastName() != null) {
            person.setLastName(token.getLastName());
        }
        if (token.getDateOfBirth() != null) {
            person.setDateOfBirth(token.getDateOfBirth());
        }
        if (token.getGender() != null) {
            person.setGender(Person.Gender.fromValue(token.getGender()));
        }

        Contact contact = account.getContact();
        if (token.getEmail() != null) {
            contact.setEmail(token.getEmail());
            contact.setEmailConfirmed(false);
        }

        Secret secret = account.getSecret();
        if (token.getPassword() != null) {
            secret.setHash(passwordEncoder.encode(token.getPassword()));
            secret.setLastModifiedDate(System.currentTimeMillis());
        }

        return accountRepository.save(account);
    }
}
