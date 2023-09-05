package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;

public class CsrfException extends ForbiddenException {

    public static final String TYPE = "BAD_CSRF";
    public static final String DESCRIPTION = "Missing or invalid csrf token.";

    public CsrfException() {
        this(HttpHeaders.EMPTY, null);
    }

    public CsrfException(HttpHeaders headers) {
        this(headers, null);
    }

    public CsrfException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public CsrfException(HttpHeaders headers, Throwable cause) {
        this(headers, new CsrfErrorBody(TYPE, DESCRIPTION), cause);
    }

    protected CsrfException(HttpHeaders headers, CsrfErrorBody body, Throwable cause) {
        super(headers, body, cause);
    }

    public static class CsrfErrorBody extends ForbiddenException.ForbiddenBody {

        protected CsrfErrorBody(String type, String description) {
            super(type, description);
        }
    }
}
