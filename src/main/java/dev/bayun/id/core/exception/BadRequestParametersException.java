package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class BadRequestParametersException extends BadRequestException {

    public static final String TYPE = "BAD_REQUEST_PARAMETERS";
    public static final String DESCRIPTION = "The provided request parameters is not valid.";

    public BadRequestParametersException(String... parameters) {
        this(null, parameters);
    }

    public BadRequestParametersException(Throwable cause, String... parameters) {
        this(HttpHeaders.EMPTY, cause, parameters);
    }

    public BadRequestParametersException(HttpHeaders headers, Throwable cause, String... parameters) {
        this(headers, new BadRequestParametersErrorBody(TYPE, DESCRIPTION, parameters), cause);
    }

    protected BadRequestParametersException(HttpHeaders headers, BadRequestParametersErrorBody body, Throwable cause) {
        super(headers, body, cause);
    }

    public static class BadRequestParametersErrorBody extends BadRequestException.BadRequestErrorBody {

        protected BadRequestParametersErrorBody(String type, String description, String... parameters) {
            super(type, description);
            setProperty("parameters", parameters);
        }
    }
}
