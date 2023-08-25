package dev.bayun.id;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bayun.id.api.schema.response.PostLoginAvailabilityResponse;
import dev.bayun.id.api.schema.response.PostLoginResponse;
import dev.bayun.id.api.schema.response.PostSignupAvailabilityResponse;
import dev.bayun.id.api.schema.response.PostSignupResponse;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.Authority;
import dev.bayun.id.core.entity.account.Person;
import dev.bayun.id.core.repository.AccountRepository;
import dev.bayun.id.util.TestAccountBuilder;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
public class APIEndpointsTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, Account> accounts;

    @BeforeAll
    public void setUp() {
        accounts = new HashMap<>();

        Account normal = accountRepository.save(new TestAccountBuilder()
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

        Account unregistered = new TestAccountBuilder()
                .id(UUID.randomUUID())
                .username("unregistered")
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
                .build();

        accounts.put(normal.getUsername(), normal);
        accounts.put(unregistered.getUsername(), unregistered);
    }

    @Test
    public void test_api_loginAvailability_ok() throws Exception {
        Account account = accounts.get("normal"); // registered

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/login/availability").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", account.getUsername());

        String expectedJsonResponse = objectMapper.writeValueAsString(new PostLoginAvailabilityResponse(true, null));

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse));
    }

    @Test
    public void test_api_login_ok() throws Exception {
        Account account = accounts.get("normal"); // registered

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/login").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", account.getUsername())
                .param("password", "password");

        String expectedJsonResponse = objectMapper.writeValueAsString(new PostLoginResponse("/"));

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse));
    }

    @Test
    public void test_api_signupAvailability_ok() throws Exception {
        Account account = accounts.get("unregistered");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/signup/availability").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", account.getUsername());

        String expectedJsonResponse = objectMapper.writeValueAsString(new PostSignupAvailabilityResponse(true));

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse));
    }

    @Test
    public void test_api_signup_ok() throws Exception {
        Account account = accounts.get("unregistered");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/api/signup").with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("username", account.getUsername())
                .param("firstName", account.getPerson().getFirstName())
                .param("lastName", account.getPerson().getLastName())
                .param("dateOfBirth", account.getPerson().getDateOfBirth())
                .param("gender", account.getPerson().getGender().name())
                .param("email", account.getContact().getEmail())
                .param("password", "password");

        String expectedJsonResponse = objectMapper.writeValueAsString(new PostSignupResponse());

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse));
    }
}
