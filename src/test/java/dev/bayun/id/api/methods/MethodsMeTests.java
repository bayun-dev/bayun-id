package dev.bayun.id.api.methods;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.Authority;
import dev.bayun.id.core.entity.account.Email;
import dev.bayun.id.core.error.Error;
import dev.bayun.id.core.repository.AccountRepository;
import dev.bayun.id.core.service.email.EmailService;
import dev.bayun.id.util.TestAccountBuilder;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureEmbeddedDatabase(type = AutoConfigureEmbeddedDatabase.DatabaseType.POSTGRES)
public class MethodsMeTests {

    private static final String USER_DETAILS_SERVICE_BEAN_NAME = "defaultAccountService";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Account user;

    private Account unregistered;

    private Account blocked;

    private Account deleted;

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
        accountRepository.save(user);

        unregistered = new TestAccountBuilder()
                .id(UUID.randomUUID())
                .username("unregistered")
                .firstName("UnregisteredFirstName")
                .lastName("UnregisteredLastName")
                .authorities(new HashSet<>(Set.of(Authority.ROLE_USER)))
                .passwordHash(passwordEncoder.encode("password"))
                .build();

        blocked = new TestAccountBuilder()
                .id(UUID.randomUUID())
                .username("blocked")
                .firstName("BlockedFirstName")
                .lastName("BlockedLastName")
                .authorities(new HashSet<>(Set.of(Authority.ROLE_BLOCKED)))
                .passwordHash(passwordEncoder.encode("password"))
                .build();
        accountRepository.save(blocked);

        deleted = new TestAccountBuilder()
                .id(UUID.randomUUID())
                .username("deleted")
                .authorities(new HashSet<>(Set.of(Authority.ROLE_DELETED)))
                .build();
        accountRepository.save(deleted);
    }

    /*
        Request without authorization.
        Expect:
            Status: 403
            Content-Type: application/json
            Type: AUTH_RESTART (AuthRestartException)
    */
    @Test
    public void test_apiMethodsMe_withoutAuthentication() throws Exception {
        Iterable<RequestBuilder> requestBuilders = Set.of(
                get(MethodsMe.URI_ME_GET_VALUE), post(MethodsMe.URI_ME_GET_VALUE),
                get(MethodsMe.URI_ME_DELETE_VALUE), post(MethodsMe.URI_ME_DELETE_VALUE),
                get(MethodsMe.URI_ME_SAVE_VALUE), post(MethodsMe.URI_ME_SAVE_VALUE),
                get(MethodsMe.URI_ME_EMAIL_CONFIRM_VALUE), post(MethodsMe.URI_ME_EMAIL_CONFIRM_VALUE)
        );

        for (RequestBuilder requestBuilder : requestBuilders) {
            this.mockMvc.perform(requestBuilder)
                    .andExpect(status().is(Error.AUTH_RESTART.getStatus()))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(containsString("\"ok\":false")))
                    .andExpect(content().string(containsString("\"type\":\"%s\"".formatted(Error.AUTH_RESTART.getType()))));
        }
    }

    /*
        Request with unsupported HTTP Method: DELETE, PATCH, PUT.
        Expect:
            Status: 400
            Content-Type: application/json
            Type: INVALID_REQUEST
     */
    @Test
    public void test_apiMethodsMe_unsupportedHttpMethod() throws Exception {
        Iterable<HttpMethod> deniedMethods = Set.of(HttpMethod.DELETE, HttpMethod.PATCH, HttpMethod.PUT);

        Iterable<String> methodsUris = Set.of(MethodsMe.URI_ME_GET_VALUE, MethodsMe.URI_ME_DELETE_VALUE,
                MethodsMe.URI_ME_SAVE_VALUE, MethodsMe.URI_ME_EMAIL_CONFIRM_VALUE);

        Set<RequestBuilder> requestBuilders = new HashSet<>();
        deniedMethods.forEach(method -> methodsUris.forEach(uri -> requestBuilders.add(request(method, uri))));

        for (RequestBuilder requestBuilder : requestBuilders) {
            this.mockMvc.perform(requestBuilder)
                    .andExpect(status().is(Error.INVALID_REQUEST.getStatus()))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(containsString("\"ok\":false")))
                    .andExpect(content().string(containsString("\"type\":\"%s\"".formatted(Error.INVALID_REQUEST.getType()))));
        }
    }

    /*
        Request with deleted account.
        Expect:
            Status: 403
            Content-Type: application/json
            Type: ACCESS_DENIED
     */
    @Test
    @WithUserDetails(value = "deleted", userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME)
    public void test_apiMethodsMe_forDeleted() throws Exception {
        Iterable<RequestBuilder> requestBuilders = Set.of(
                get(MethodsMe.URI_ME_GET_VALUE), post(MethodsMe.URI_ME_GET_VALUE),
                get(MethodsMe.URI_ME_DELETE_VALUE), post(MethodsMe.URI_ME_DELETE_VALUE),
                get(MethodsMe.URI_ME_SAVE_VALUE), post(MethodsMe.URI_ME_SAVE_VALUE),
                get(MethodsMe.URI_ME_EMAIL_CONFIRM_VALUE), post(MethodsMe.URI_ME_EMAIL_CONFIRM_VALUE)
        );

        for (RequestBuilder requestBuilder : requestBuilders) {
            this.mockMvc.perform(requestBuilder)
                    .andExpect(status().is(Error.ACCESS_DENIED.getStatus()))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(containsString("\"ok\":false")))
                    .andExpect(content().string(containsString("\"type\":\"%s\"".formatted(Error.ACCESS_DENIED.getType()))));
        }
    }

    /*
        Request with blocked account.
        Expect:
            Status: 403
            Content-Type: application/json
            Type: ACCESS_DENIED
     */
    @Test
    @WithUserDetails(value = "blocked", userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME)
    public void test_apiMethodsMe_forBlocked() throws Exception {
        Iterable<RequestBuilder> requestBuilders = Set.of(
                get(MethodsMe.URI_ME_GET_VALUE), post(MethodsMe.URI_ME_GET_VALUE),
                get(MethodsMe.URI_ME_DELETE_VALUE), post(MethodsMe.URI_ME_DELETE_VALUE),
                get(MethodsMe.URI_ME_SAVE_VALUE), post(MethodsMe.URI_ME_SAVE_VALUE),
                get(MethodsMe.URI_ME_EMAIL_CONFIRM_VALUE), post(MethodsMe.URI_ME_EMAIL_CONFIRM_VALUE)
        );

        for (RequestBuilder requestBuilder : requestBuilders) {
            this.mockMvc.perform(requestBuilder)
                    .andExpect(status().is(Error.ACCESS_DENIED.getStatus()))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(containsString("\"ok\":false")))
                    .andExpect(content().string(containsString("\"type\":\"%s\"".formatted(Error.ACCESS_DENIED.getType()))));
        }
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME)
    public void test_apiMethodsMe_get() throws Exception {
        Iterable<RequestBuilder> requestBuilders = Set.of(
                get(MethodsMe.URI_ME_GET_VALUE), post(MethodsMe.URI_ME_GET_VALUE));

        Account me = accountRepository.findByUsername(user.getUsername()).orElseThrow();
        Email email = emailService.loadById(me.getEmailId());
        MethodsMe.MeGetResponse expectedResponseBody = new MethodsMe.MeGetResponse(me,
                email == null ? null : emailService.toHidden(email.getEmail()),
                email == null ? null : email.isConfirmed());
        String exceptedResponseJsonBody = objectMapper.writeValueAsString(expectedResponseBody);

        for (RequestBuilder requestBuilder : requestBuilders) {
            this.mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(exceptedResponseJsonBody));
        }
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME)
    public void test_apiMethodsMe_save() throws Exception {
        Iterable<MockHttpServletRequestBuilder> requestBuilders = Set.of(
                get(MethodsMe.URI_ME_SAVE_VALUE), post(MethodsMe.URI_ME_SAVE_VALUE));

        for (MockHttpServletRequestBuilder requestBuilder : requestBuilders) {
            requestBuilder.param("firstName", unregistered.getFirstName());
            requestBuilder.param("lastName", unregistered.getLastName());
        }

        String exceptedResponseJsonBody = objectMapper.writeValueAsString(new MethodsMe.MeSaveResponse());

        for (RequestBuilder requestBuilder : requestBuilders) {
            this.mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(exceptedResponseJsonBody));

            Account account = accountRepository.findByUsername("user").orElseThrow();
            Assertions.assertEquals(unregistered.getFirstName(), account.getFirstName());
            Assertions.assertEquals(unregistered.getLastName(), account.getLastName());
            accountRepository.save(user);
        }
    }

    @Test
    @WithUserDetails(value = "user", userDetailsServiceBeanName = USER_DETAILS_SERVICE_BEAN_NAME)
    public void test_apiMethodsMe_save_invalid_request_params() throws Exception {
        Iterable<MockHttpServletRequestBuilder> requestBuilders = Set.of(
                get(MethodsMe.URI_ME_SAVE_VALUE), post(MethodsMe.URI_ME_SAVE_VALUE));

        for (MockHttpServletRequestBuilder requestBuilder : requestBuilders) {
            requestBuilder.param("firstName", "$");
            requestBuilder.param("lastName", "$");
        }

        for (RequestBuilder requestBuilder : requestBuilders) {
            this.mockMvc.perform(requestBuilder)
                    .andExpect(status().is(Error.INVALID_REQUEST_PARAM.getStatus()))
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(content().string(containsString("\"ok\":false")))
                    .andExpect(content().string(containsString("\"type\":\"%s\"".formatted(Error.INVALID_REQUEST_PARAM.getType()))));
        }
    }
}
