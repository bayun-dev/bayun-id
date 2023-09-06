package dev.bayun.id.core.exception;

public class UsernameOccupiedException extends RuntimeException {

    public UsernameOccupiedException(String username) {
        this(username, null);
    }

    public UsernameOccupiedException(String username, Throwable cause) {
        super("The username (%s) is already in use.".formatted(username), cause);
    }
}
