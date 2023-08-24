package dev.bayun.id.api.endpoint.login;

import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.api.schema.request.PostLoginAvailabilityRequest;
import dev.bayun.id.api.schema.response.PostLoginAvailabilityResponse;
import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class PostLoginAvailability {

    private AccountService accountService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(
            path = "/api/login/availability",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PostLoginAvailabilityResponse handle(@Validated PostLoginAvailabilityRequest body, BindingResult bindingResult)
            throws BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        try {
            Account account = accountService.loadUserByUsername(body.getUsername());
            if (!account.isEnabled()) {
                return new PostLoginAvailabilityResponse(false, Errors.ACCOUNT_DELETED_CODE);
            }
            if (!account.isAccountNonLocked()) {
                return new PostLoginAvailabilityResponse(false, Errors.ACCOUNT_BLOCKED_CODE);
            }

            return new PostLoginAvailabilityResponse(true, null);
        } catch (UsernameNotFoundException exception) {
            return new PostLoginAvailabilityResponse(false, Errors.USERNAME_NOT_OCCUPIED_CODE);
        }
    }
}
