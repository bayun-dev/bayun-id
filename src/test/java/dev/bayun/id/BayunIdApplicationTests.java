package dev.bayun.id;

import dev.bayun.id.core.entity.account.Authority;
import dev.bayun.id.core.repository.AccountRepository;
import dev.bayun.id.util.TestAccountBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
class BayunIdApplicationTests {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    void contextLoads() {
        try {
            accountRepository.save(new TestAccountBuilder()
                    .id(UUID.randomUUID())
                    .username("msyaskov")
                    .firstName("FirstName")
                    .lastName("LastName")
                    .dateOfBirth("01.01.1999")
                    .gender(Person.Gender.MALE)
                    .email("mail@example.com")
                    .emailConfirmed(false)
                    .registrationDate(System.currentTimeMillis())
                    .authorities(new HashSet<>(Set.of(Authority.ROLE_USER)))
                    .deactivated(false)
                    .secretHash("password")
                    .secretLastModified(System.currentTimeMillis())
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
