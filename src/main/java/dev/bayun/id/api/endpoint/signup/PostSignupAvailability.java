package dev.bayun.id.api.endpoint.signup;

import dev.bayun.id.api.schema.request.PostSignupAvailabilityRequest;
import dev.bayun.id.api.schema.response.PostSignupAvailabilityResponse;
import dev.bayun.id.core.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class PostSignupAvailability {

    private final AccountService accountService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(
            path = "/api/signup/availability",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PostSignupAvailabilityResponse handle(@Validated PostSignupAvailabilityRequest body, BindingResult bindingResult)
            throws BindException {

        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        if (accountService.exists(body.getUsername())) {
            return new PostSignupAvailabilityResponse(false);
        }

        return new PostSignupAvailabilityResponse(true);
    }
}
