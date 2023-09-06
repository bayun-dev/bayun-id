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
    }

}
