package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseErrorResponseException {

    public static final String TYPE = "BAD_REQUEST";
    public static final String DESCRIPTION = "The request is incorrect.";

    public BadRequestException() {
        this(null);
    }

    public BadRequestException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public BadRequestException(HttpHeaders headers, Throwable cause) {
        this(headers, new BadRequestErrorBody(TYPE, DESCRIPTION), cause);
    }

    protected BadRequestException(HttpHeaders headers, BadRequestErrorBody body, Throwable cause) {
        super(HttpStatus.BAD_REQUEST.value(), headers, body, cause);
    }

    public static class BadRequestErrorBody extends BaseErrorBody {

        protected BadRequestErrorBody(String type, String description) {
            super(HttpStatus.BAD_REQUEST.value(), type, description);
        }

    }

}
