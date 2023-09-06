package dev.bayun.id.core.service;

import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.AccountCreateToken;
import dev.bayun.id.core.entity.account.AccountUpdateToken;
import dev.bayun.id.core.entity.account.Authority;
import dev.bayun.id.core.exception.AccountNotFoundException;
import dev.bayun.id.core.repository.AccountRepository;
import dev.bayun.id.core.util.UUIDGenerator;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

@Service
@Transactional
@AllArgsConstructor
public class DefaultAccountService implements AccountService {

    public static final Collection<Authority> AUTHORITY_FOR_DELETED = Collections.singleton(Authority.ROLE_DELETED);

    public static final Collection<Authority> AUTHORITY_FOR_BLOCKED = Collections.singleton(Authority.ROLE_BLOCKED);

    public static final Collection<Authority> AUTHORITY_FOR_NEW_USER = Collections.singleton(Authority.ROLE_USER);

    public static final Collection<Authority> AUTHORITY_FOR_ADMIN = Set.of(Authority.ROLE_USER, Authority.ROLE_ADMIN);

    @Setter
    private AccountRepository accountRepository;

    @Setter
    private PasswordEncoder passwordEncoder;

    public Account block(UUID id) {
        Account account = loadUserById(id);
        account.setAuthorities(new HashSet<>(AUTHORITY_FOR_BLOCKED));

        return accountRepository.save(account);
    }

    @Override
    public Account create(AccountCreateToken token) {
        Account account = new Account();
        account.setAuthorities(new HashSet<>(AUTHORITY_FOR_NEW_USER));
        account.setAvatarId(null);
        account.setEmailId(null);
        account.setFirstName(token.getFirstName());
        account.setId(UUIDGenerator.getV7());
        account.setLastName(token.getLastName());
        account.setPasswordHash(passwordEncoder.encode(token.getPassword()));
        account.setUsername(token.getUsername());

        return accountRepository.save(account);
    }

    @Override
    public Account delete(UUID id) {
        Assert.notNull(id, "The id must not be null");

        Account account = loadUserById(id);
        account.setAuthorities(new HashSet<>(AUTHORITY_FOR_DELETED));

        account.setAvatarId(null);
        account.setEmailId(null);
        account.setFirstName(null);
        account.setLastName(null);
        account.setPasswordHash(null);

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
                .orElseThrow(() -> new AccountNotFoundException("Account (id=%s) not found".formatted(id)));
    }

    @Override
    public Account loadUserByUsername(String username) {
        Assert.notNull(username, "The username must not be null");
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "account with username (%s) not found".formatted(username)));
    }

    // return raw password
    public String resetPassword(UUID id) {
        Account account = loadUserById(id);

        String rawPassword = generatePassword();
        account.setPasswordHash(passwordEncoder.encode(rawPassword));

        accountRepository.save(account);

        return rawPassword;
    }

    @Override
    public Account update(UUID id, AccountUpdateToken token) {
        Assert.notNull(id, "The id must not be null");
        Assert.notNull(id, "The token must not be null");

        Account account = loadUserById(id);

        if (token.isDropAvatar()) {
            account.setAvatarId(null);
        } else if (token.getAvatarId() != null) {
            account.setAvatarId(token.getAvatarId());
        }

        if (token.getEmailId() != null) {
            account.setEmailId(token.getEmailId());
        }

        if (token.getFirstName() != null) {
            account.setFirstName(token.getFirstName());
        }

        if (token.getLastName() != null) {
            account.setLastName(token.getLastName());
        }

        if (token.getPassword() != null) {
            account.setPasswordHash(passwordEncoder.encode(token.getPassword()));
        }

        return accountRepository.save(account);
    }

    private String generatePassword() {
        final int length = 16;
        String alphabet = "aA0bB1cC2dD3eE4fF5gG6hH7iI8jJ9kK!lL@mM#nN$oO%pP^qQ&rR*sS(tT)uU_vV+wW-xX=yY?zZ,";

        StringBuilder builder = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < length; i++) {
            builder.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }

        return builder.toString();
    }
}
