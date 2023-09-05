package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;

public class PasswordInvalidException extends BadRequestException {

    public static final String TYPE = "PASSWORD_INVALID";
    public static final String DESCRIPTION = "The provided password is invalid.";

    public PasswordInvalidException() {
        this(HttpHeaders.EMPTY, null);
    }

    public PasswordInvalidException(HttpHeaders headers) {
        this(headers, null);
    }

    public PasswordInvalidException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public PasswordInvalidException(HttpHeaders headers, Throwable cause) {
        this(headers, new PasswordInvalidErrorBody(TYPE, DESCRIPTION), cause);
    }

    protected PasswordInvalidException(HttpHeaders headers, PasswordInvalidErrorBody body, Throwable cause) {
        super(headers, body, cause);
    }

    public static class PasswordInvalidErrorBody extends BadRequestErrorBody {

        protected PasswordInvalidErrorBody(String type, String description) {
            super(type, description);
        }
    }
}
