package dev.bayun.id;

import dev.bayun.id.core.configuration.PasswordEncoderConfiguration;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.Authority;
import dev.bayun.id.core.entity.account.AccountUpdateToken;
import dev.bayun.id.core.entity.account.AccountCreateToken;
import dev.bayun.id.core.repository.AccountRepository;
import dev.bayun.id.core.repository.AvatarRepository;
import dev.bayun.id.core.service.AccountService;
import dev.bayun.id.core.service.AvatarService;
import dev.bayun.id.core.service.DefaultAccountService;
import dev.bayun.id.core.service.email.EmailService;
import dev.bayun.id.util.TestAccountBuilder;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@DataJpaTest
@Import({PasswordEncoderConfiguration.class, DefaultAccountService.class, AvatarService.class, AvatarRepository.class})
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
public class AccountServiceTests {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @MockBean
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        accountRepository.save(new TestAccountBuilder()
                .id(UUID.randomUUID())
                .username("normal")
                .firstName("FirstName")
                .lastName("LastName")
                .dateOfBirth("01.01.1999")
                .gender(Person.Gender.MALE)
                .email("mail@example.com")
                .emailConfirmed(false)
                .registrationDate(System.currentTimeMillis())
                .authorities(new HashSet<>(Set.of(Authority.ROLE_USER)))
                .deactivated(false)
                .secretHash(passwordEncoder.encode("password"))
                .secretLastModified(System.currentTimeMillis())
                .build());
    }

    @Test
    public void test_create() {
        AccountCreateToken token = new AccountCreateToken();
        token.setUsername("Username");
        token.setFirstName("FirstName");
        token.setLastName("LastName");
        token.setDateOfBirth("01.01.1999");
        token.setGender(Person.Gender.FEMALE.name());
        token.setEmail("mail@example.com");
        token.setPassword("password");

        Account created = accountService.create(token);

        assertEquals(token.getUsername(), created.getUsername());
        assertEquals(token.getFirstName(), created.getPerson().getFirstName());
        assertEquals(token.getLastName(), created.getPerson().getLastName());
        assertEquals(token.getDateOfBirth(), created.getPerson().getDateOfBirth());
        assertEquals(Person.Gender.fromValue(token.getGender()), created.getPerson().getGender());
        assertEquals(token.getEmail(), created.getContact().getEmail());
        assertTrue(passwordEncoder.matches(token.getPassword(), created.getPassword()));
        assertFalse(created.getDeactivation().isDeactivated());
    }

    @Test
    public void test_delete() throws JsonProcessingException {
        Account account = accountRepository.findByUsername("normal").orElseThrow();
        Account deleted = accountService.delete(account.getId());

        assertNull(deleted.getPerson());
        assertNull(deleted.getContact());
        assertNull(deleted.getSecret());
        for (Authority authority : deleted.getAuthorities()) {
            assertEquals(Authority.ROLE_DELETED, authority);
        }
        assertTrue(deleted.getDeactivation().isDeactivated());
        assertEquals(Deactivation.Reason.DELETED, deleted.getDeactivation().getReason());
        assertNotNull(deleted.getDeactivation().getDate());
        assertNotEquals(0, deleted.getDeactivation().getDate());

        log.info(new ObjectMapper().writeValueAsString(deleted));
        log.info(deleted.toString());
    }

    @Test
    public void test_load() {
        assertDoesNotThrow(() -> {
            Account account = accountService.loadUserByUsername("normal");
            assertNotNull(account);

            assertNotNull(accountService.loadUserById(account.getId()));
        });
    }

    @Test
    public void test_update() {
        Account account = accountRepository.findByUsername("normal").orElseThrow();

        AccountUpdateToken token = new AccountUpdateToken();
        token.setFirstName("ChangedFirstName");
        token.setLastName("ChangedLastName");
        token.setDateOfBirth("ChangedDateOfBirth"); // invalid, but.. no validation
        token.setGender(Person.Gender.MALE.name());
        token.setEmail("changeemail@example.com");
        token.setPassword("changedpassword");

        Account updated = accountService.update(account.getId(), token);

        assertEquals(token.getFirstName(), updated.getPerson().getFirstName());
        assertEquals(token.getLastName(), updated.getPerson().getLastName());
        assertEquals(token.getDateOfBirth(), updated.getPerson().getDateOfBirth());
        assertEquals(Person.Gender.fromValue(token.getGender()), updated.getPerson().getGender());
        assertEquals(token.getEmail(), updated.getContact().getEmail());
        assertTrue(passwordEncoder.matches(token.getPassword(), updated.getPassword()));
    }
}
