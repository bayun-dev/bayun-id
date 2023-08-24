package dev.bayun.id.core.exception;

public class UsernameOccupiedException extends RuntimeException {

    public UsernameOccupiedException() {
        super();
    }

    public UsernameOccupiedException(String message) {
        super(message);
    }

    public UsernameOccupiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameOccupiedException(Throwable cause) {
        super(cause);
    }
}
