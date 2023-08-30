package dev.bayun.id.core.service;

import dev.bayun.id.core.entity.account.*;
import dev.bayun.id.core.exception.AccountNotFoundException;
import dev.bayun.id.core.exception.AccountRegistrationException;
import dev.bayun.id.core.exception.AccountUpdateException;
import dev.bayun.id.core.modal.AccountCreateToken;
import dev.bayun.id.core.modal.AccountUpdateToken;
import dev.bayun.id.core.repository.AccountRepository;
import dev.bayun.id.core.repository.AvatarRepository;
import dev.bayun.id.core.repository.EmailUpdateTokenRepository;
import dev.bayun.id.core.service.email.EmailContext;
import dev.bayun.id.core.service.email.EmailService;
import dev.bayun.id.core.util.converter.UUIDGenerator;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
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
public class DefaultAccountService implements AccountService {

    @Setter
    private AccountRepository accountRepository;

    @Setter
    private AvatarService avatarService;

    @Setter
    private EmailService emailService;

    @Setter
    private EmailUpdateTokenRepository emailUpdateTokenRepository;

    @Setter
    private PasswordEncoder passwordEncoder;

    @Value("${server.host}")
    private String serverHost;

    public DefaultAccountService(AccountRepository accountRepository, AvatarService avatarService, EmailService emailService, EmailUpdateTokenRepository emailUpdateTokenRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.emailService = emailService;
        this.emailUpdateTokenRepository = emailUpdateTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.avatarService = avatarService;
    }

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

        account.setAvatarId("default");
        account.setPerson(null);
        account.setContact(null);
        account.setSecret(null);

        Set<Authority> authorities = new HashSet<>();
        authorities.add(Authority.ROLE_DELETED);
        account.setAuthorities(authorities);

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
                .orElseThrow(AccountNotFoundException::new);
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

        try {
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
                EmailUpdateToken emailUpdateToken = new EmailUpdateToken();
                emailUpdateToken.setId(UUID.randomUUID());
                emailUpdateToken.setAccountId(account.getId());
                emailUpdateToken.setDate(System.currentTimeMillis());
                emailUpdateToken.setEmail(token.getEmail());
                saveEmailUpdateToken(emailUpdateToken);
                sendConfirmationEmail(emailUpdateToken, account.getPerson().getFirstName());

                contact.setEmail(token.getEmail());
                contact.setEmailConfirmed(false);
            }

            Secret secret = account.getSecret();
            if (token.getPassword() != null) {
                secret.setHash(passwordEncoder.encode(token.getPassword()));
                secret.setLastModifiedDate(System.currentTimeMillis());
            }

            if (token.getAvatar() != null) {
                Avatar avatar = avatarService.save(token.getAvatar().getBytes());
                account.setAvatarId(avatar.getId());
            }

            return accountRepository.save(account);
        } catch (Exception e) {
            throw new AccountUpdateException(e);
        }
    }

    private void saveEmailUpdateToken(EmailUpdateToken token) {
        emailUpdateTokenRepository.deleteAllByAccountId(token.getAccountId());
        emailUpdateTokenRepository.save(token);
    }

    private void sendConfirmationEmail(EmailUpdateToken token, String firstName) {
        String hiddenEmail = getHiddenEmail(token.getEmail());
        EmailContext context = new EmailContext("Linking an email to an account", "email-confirm");
        context.getVariables().put("link", serverHost + "/email-confirm?hash="+token.getId());
        context.getVariables().put("firstName", firstName);
        context.getVariables().put("email", hiddenEmail);
        try {
            emailService.send(token.getEmail(), context);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getHiddenEmail(String email) {
        String[] emailParts = email.split("@");
        Assert.state(emailParts.length == 2, "invalid email");
        String local = emailParts[0];
        String domain = emailParts[1];

        return local.charAt(0) + (local.length() > 2 ? String.valueOf(local.charAt(1)) : "") + "***@" + domain;
    }

    @Override
    public void emailConfirm(UUID id, String tokenId) {
        Account account = loadUserById(id);
        UUID emailUpdateTokenId = UUID.fromString(tokenId);
        emailUpdateTokenRepository.findById(emailUpdateTokenId).ifPresent(token -> {
            if (token.getAccountId().equals(account.getId())) {
                account.getContact().setEmail(token.getEmail());
                account.getContact().setEmailConfirmed(true);
            }
        });
        emailUpdateTokenRepository.deleteAllByAccountId(account.getId());
    }

    @Override
    public void setDefaultAvatar(UUID id) {
        Account account = loadUserById(id);
        account.setAvatarId("default");
        accountRepository.save(account);
    }
}
