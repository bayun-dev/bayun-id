package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

public class ForbiddenException extends BaseErrorResponseException {

    public static final String TYPE = "FORBIDDEN";

    public static final String DESCRIPTION = "Access denied.";

    public ForbiddenException() {
        this(HttpHeaders.EMPTY, new ForbiddenBody(TYPE, DESCRIPTION), null);
    }

    public ForbiddenException(HttpHeaders headers) {
        this(headers, null);
    }

    public ForbiddenException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public ForbiddenException(HttpHeaders headers, Throwable cause) {
        this(headers, new ForbiddenBody(TYPE, DESCRIPTION), cause);
    }

    protected ForbiddenException(HttpHeaders headers, ForbiddenBody body, Throwable cause) {
        super(HttpStatus.FORBIDDEN.value(), headers, body, cause);
    }

    public static class ForbiddenBody extends BaseErrorBody {

        protected ForbiddenBody(String type, String description) {
            super(HttpStatus.FORBIDDEN.value(), type, description);
        }
    }
}
