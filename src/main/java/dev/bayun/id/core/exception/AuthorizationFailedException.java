package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;

public class AuthorizationFailedException extends ForbiddenException {

    public static final String TYPE = "AUTH_FAILED";
    public static final String DESCRIPTION = "Failure while authorization.";

    public AuthorizationFailedException() {
        this(HttpHeaders.EMPTY, null);
    }

    public AuthorizationFailedException(HttpHeaders headers) {
        this(headers, null);
    }

    public AuthorizationFailedException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public AuthorizationFailedException(HttpHeaders headers, Throwable cause) {
        this(headers, new AuthorizationFailedErrorBody(TYPE, DESCRIPTION), cause);
    }

    protected AuthorizationFailedException(HttpHeaders headers, AuthorizationFailedErrorBody body, Throwable cause) {
        super(headers, body, cause);
    }

    public static class AuthorizationFailedErrorBody extends ForbiddenBody {

        protected AuthorizationFailedErrorBody(String type, String description) {
            super(type, description);
        }
    }
}
