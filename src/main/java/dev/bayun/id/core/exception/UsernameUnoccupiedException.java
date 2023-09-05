package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;

public class UsernameUnoccupiedException extends BadRequestException {

    public static final String TYPE = "USERNAME_UNOCCUPIED";
    public static final String DESCRIPTION = "The username is not yet being used.";

    public UsernameUnoccupiedException() {
        this(HttpHeaders.EMPTY, null);
    }

    public UsernameUnoccupiedException(HttpHeaders headers) {
        this(headers, null);
    }

    public UsernameUnoccupiedException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public UsernameUnoccupiedException(HttpHeaders headers, Throwable cause) {
        this(headers, new UsernameUnOccupiedErrorBody(TYPE, DESCRIPTION), cause);
    }

    protected UsernameUnoccupiedException(HttpHeaders headers, UsernameUnOccupiedErrorBody body, Throwable cause) {
        super(headers, body, cause);
    }

    public static class UsernameUnOccupiedErrorBody extends BadRequestErrorBody {

        protected UsernameUnOccupiedErrorBody(String type, String description) {
            super(type, description);
        }
    }
}
