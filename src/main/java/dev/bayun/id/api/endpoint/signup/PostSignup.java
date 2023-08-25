package dev.bayun.id.api.endpoint.signup;

import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.api.schema.request.PostSignupRequest;
import dev.bayun.id.api.schema.response.ErrorResponse;
import dev.bayun.id.api.schema.response.PostSignupResponse;
import dev.bayun.id.core.entity.account.Person;
import dev.bayun.id.core.exception.UsernameOccupiedException;
import dev.bayun.id.core.service.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@AllArgsConstructor
public class PostSignup {

    private final AccountService accountService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(
            path = "/api/signup",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PostSignupResponse handle(@Validated PostSignupRequest body, BindingResult bindingResult) throws BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        if (accountService.exists(body.getUsername())) {
            throw new UsernameOccupiedException(body.getUsername());
        }

        Person person = new Person();
        person.setFirstName(body.getFirstName());
        person.setLastName(body.getLastName());
        person.setDateOfBirth(body.getDateOfBirth());
        person.setGender(Person.Gender.fromValue(body.getGender()));
        accountService.create(body.getUsername(), person, body.getPassword(), body.getEmail());

        return new PostSignupResponse();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameOccupiedException.class)
    public ResponseEntity<ErrorResponse> usernameOccupiedExceptionHandle(UsernameOccupiedException exception) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(Errors.USERNAME_OCCUPIED));
    }
}
