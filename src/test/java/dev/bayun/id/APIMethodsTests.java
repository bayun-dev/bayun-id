package dev.bayun.id;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.api.schema.response.*;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.Authority;
import dev.bayun.id.core.repository.AccountRepository;
import dev.bayun.id.util.TestAccountBuilder;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
public class APIMethodsTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, Account> accounts;

    private Account user;

    @BeforeAll
    public void setUp() {
        user = new TestAccountBuilder()
                .id(UUID.randomUUID())
                .username("user")
                .firstName("UserFirstName")
                .lastName("UserLastName")
                .authorities(new HashSet<>(Set.of(Authority.ROLE_USER)))
                .passwordHash(passwordEncoder.encode("password"))
                .build();
    }

    @Test
    public void test_apiMethods_error_METHOD_INVALID() throws Exception {
//        RequestBuilder requestBuilder = MockMvcRequestBuilders
//                .get("/api/methods/me.get")
//                .accept(MediaType.APPLICATION_XML_VALUE);
//
//        this.mockMvc.perform(requestBuilder)
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
//                .andExpect(content().string(containsString("\"ok\":false")))
//                .andExpect(content().string(containsString("\"type\":\"AUTH_RESTART\"")));
    }

    @Test
    public void test_apiMethods_meGet_withoutAuthentication() throws Exception {
        /*
            HTTP Method: GET
            Accept: application/json

            Expect:
                Status: 500
                Content-Type: application/json
                Type: AUTH_RESTART
         */
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/methods/me.get")
                .accept(MediaType.APPLICATION_JSON);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().string(containsString("\"ok\":false")))
                .andExpect(content().string(containsString("\"type\":\"AUTH_RESTART\"")));

        /*
            HTTP Method: GET
            Accept: not application/json

            Expect:
                Status: 302
                Location: /login
         */

    }

    @Test
    @WithUserDetails(value = "normal", userDetailsServiceBeanName = "defaultAccountService")
    public void test_api_deleteAccountsById_me_ok() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/accounts/me")
                .accept(MediaType.APPLICATION_JSON);

        String expectedJsonResponse = objectMapper.writeValueAsString(new DeleteAccountsByIdResponse());

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse));
    }

    @Test
    @WithUserDetails(value = "normal", userDetailsServiceBeanName = "defaultAccountService")
    public void test_api_getAccountsById_ok() throws Exception {
        Account account = accounts.get("normal");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/accounts/"+account.getId())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);

        String expectedJsonResponse = objectMapper.writeValueAsString(account);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse));

        log.info(expectedJsonResponse);
    }

    @Test
    @WithUserDetails(value = "normal", userDetailsServiceBeanName = "defaultAccountService")
    public void test_api_getAccountsById_notFound() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/accounts/"+UUID.randomUUID())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString(Errors.ACCOUNT_NOT_FOUND_CODE)));
    }

    @Test
    @WithUserDetails(value = "normal", userDetailsServiceBeanName = "defaultAccountService")
    public void test_api_getAccountsById_me_ok() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/api/accounts/me")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);

        String expectedJsonResponse = objectMapper.writeValueAsString(accounts.get("normal"));

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse));
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
    @WithUserDetails(value = "normal", userDetailsServiceBeanName = "defaultAccountService")
    public void test_api_patchAccountsById_me_ok() throws Exception {
        Account normal = accounts.get("normal");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/api/accounts/me").with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("firstName", "NewFirstname")
                .param("lastName", "NewLastName")
                .param("dateOfBirth", "02.02.2000")
                .param("gender", "male")
                .param("email", "newmail@example.com")
                .param("password", "new-password");

        String expectedJsonResponse = objectMapper.writeValueAsString(new PatchAccountsByIdResponse(true));

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse));

        Assertions.assertNotEquals(normal, accountRepository.findByUsername("normal").orElseThrow());
    }

    @Test
    @WithUserDetails(value = "normal", userDetailsServiceBeanName = "defaultAccountService")
    public void test_api_patchAccountsById_me_nonParams_ok() throws Exception {
        Account normal = accounts.get("normal");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/api/accounts/me").with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED);

        String expectedJsonResponse = objectMapper.writeValueAsString(new PatchAccountsByIdResponse(true));

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse));

        Assertions.assertEquals(normal, accountRepository.findByUsername("normal").orElseThrow());
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
//                .param("firstName", account.getPerson().getFirstName())
//                .param("lastName", account.getPerson().getLastName())
//                .param("dateOfBirth", account.getPerson().getDateOfBirth())
//                .param("gender", account.getPerson().getGender().name())
//                .param("email", account.getContact().getEmail())
                .param("password", "password");

        String expectedJsonResponse = objectMapper.writeValueAsString(new PostSignupResponse());

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJsonResponse));
    }

    @Test
    @WithUserDetails(value = "normal", userDetailsServiceBeanName = "defaultAccountService")
    public void test_post_api_accounts_byId_avatar() throws Exception {
        Account normal = accounts.get("normal");

        ClassPathResource resource = new ClassPathResource("avatar.jpg");
        byte[] origin = resource.getInputStream().readAllBytes();

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .multipart(HttpMethod.POST, "/api/accounts/me/avatar").file("avatar", origin).with(csrf())
                .contentType(MediaType.MULTIPART_FORM_DATA);

        this.mockMvc.perform(requestBuilder)
                .andExpect(status().isOk());
    }
}
