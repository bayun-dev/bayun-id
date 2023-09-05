package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class InternalErrorException extends BaseErrorResponseException {

    public static final String TYPE = "INTERNAL_ERROR";
    public static final String DESCRIPTION = "Internal server error.";

    public InternalErrorException() {
        this(HttpHeaders.EMPTY, null);
    }

    public InternalErrorException(HttpHeaders headers) {
        this(headers, null);
    }

    public InternalErrorException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public InternalErrorException(HttpHeaders headers, Throwable cause) {
        this(headers, new InternalErrorBody(TYPE, DESCRIPTION), cause);
    }

    protected InternalErrorException(HttpHeaders headers, InternalErrorBody body, Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR.value(), headers, body, cause);
    }

    public static class InternalErrorBody extends BaseErrorBody {

        protected InternalErrorBody(String type, String description) {
            super(HttpStatus.INTERNAL_SERVER_ERROR.value(), type, description);
        }
    }
}
