package dev.bayun.id.core.exception;

public class EmailNotConfirmedException extends RuntimeException {

    public EmailNotConfirmedException(String message) {
        this(message, null);
    }

    public EmailNotConfirmedException(String message, Throwable cause) {
        super(message, cause);
    }
}
