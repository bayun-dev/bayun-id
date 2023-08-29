package dev.bayun.id.core.exception;

public class AvatarNotFoundException extends RuntimeException {

    public AvatarNotFoundException() {
        super();
    }

    public AvatarNotFoundException(String message) {
        super(message);
    }

    public AvatarNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AvatarNotFoundException(Throwable cause) {
        super(cause);
    }
}
