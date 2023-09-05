package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class AuthRestartException extends InternalErrorException {

    public static final String TYPE = "AUTH_RESTART";
    public static final String DESCRIPTION = "Restart the authorization process.";

    public AuthRestartException() {
        this(null);
    }

    public AuthRestartException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public AuthRestartException(HttpHeaders headers, Throwable cause) {
        this(headers, new AuthRestartErrorBody(TYPE, DESCRIPTION), cause);
    }

    protected AuthRestartException(HttpHeaders headers, AuthRestartErrorBody body, Throwable cause) {
        super(headers, body, cause);
    }

    public static class AuthRestartErrorBody extends InternalErrorBody {

        protected AuthRestartErrorBody(String type, String description) {
            super(type, description);
        }

    }

}
