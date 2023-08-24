package dev.bayun.id.api;

import dev.bayun.id.api.schema.Error;
import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.api.schema.response.ErrorResponse;
import dev.bayun.id.core.exception.UsernameOccupiedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice(basePackages = "dev.bayun.id.api")
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindExceptionHandle(BindException exception) {
        Set<Error> errors = new HashSet<>();
        if (exception.getBindingResult().hasErrors()) {
            for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
                errors.add(Errors.getByCode(fieldError.getDefaultMessage()));
            }
        }

        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(errors));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UsernameOccupiedException.class)
    public ResponseEntity<ErrorResponse> usernameUnavailableExceptionHandle(UsernameOccupiedException exception) {
        return ResponseEntity.badRequest()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(Errors.USERNAME_OCCUPIED));
    }
}
