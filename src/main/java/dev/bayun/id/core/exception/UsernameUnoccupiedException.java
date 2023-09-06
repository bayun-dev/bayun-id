package dev.bayun.id.core.exception;

public class UsernameUnoccupiedException extends RuntimeException {

    public UsernameUnoccupiedException(String username) {
        this(username, null);
    }

    public UsernameUnoccupiedException(String username, Throwable cause) {
        super("The username (%s) is not yet being used".formatted(username), cause);
    }
}
