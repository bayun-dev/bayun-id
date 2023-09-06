package dev.bayun.id.core.exception;

public class AccountNotFoundException extends RuntimeException {

    public AccountNotFoundException(String message) {
        this(message, null);
    }

    public AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
