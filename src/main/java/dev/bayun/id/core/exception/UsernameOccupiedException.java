package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;

public class UsernameOccupiedException extends BadRequestException {

    public static final String TYPE = "USERNAME_OCCUPIED";
    public static final String DESCRIPTION = "The username is already in use.";

    public UsernameOccupiedException() {
        this(HttpHeaders.EMPTY, null);
    }

    public UsernameOccupiedException(HttpHeaders headers) {
        this(headers, null);
    }

    public UsernameOccupiedException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public UsernameOccupiedException(HttpHeaders headers, Throwable cause) {
        this(headers, new UsernameOccupiedErrorBody(TYPE, DESCRIPTION), cause);
    }

    protected UsernameOccupiedException(HttpHeaders headers, UsernameOccupiedErrorBody body, Throwable cause) {
        super(headers, body, cause);
    }

    public static class UsernameOccupiedErrorBody extends BadRequestErrorBody {

        protected UsernameOccupiedErrorBody(String type, String description) {
            super(type, description);
        }
    }
}
