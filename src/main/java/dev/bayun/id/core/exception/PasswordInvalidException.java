package dev.bayun.id.core.exception;

public class PasswordInvalidException extends RuntimeException {

    public PasswordInvalidException(String message) {
        this(message, null);
    }

    public PasswordInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
