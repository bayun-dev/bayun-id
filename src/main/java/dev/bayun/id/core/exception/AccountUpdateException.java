package dev.bayun.id.core.exception;

public class AccountUpdateException extends RuntimeException {

    public AccountUpdateException() {
        super();
    }

    public AccountUpdateException(String message) {
        super(message);
    }

    public AccountUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountUpdateException(Throwable cause) {
        super(cause);
    }
}
