package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;

public class EmailNotConfirmedException extends ForbiddenException {

    public static final String TYPE = "EMAIL_NOT_CONFIRMED";
    public static final String DESCRIPTION = "Email not specified or not confirmed.";

    public EmailNotConfirmedException() {
        this(HttpHeaders.EMPTY, null);
    }

    public EmailNotConfirmedException(HttpHeaders headers) {
        this(headers, null);
    }

    public EmailNotConfirmedException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public EmailNotConfirmedException(HttpHeaders headers, Throwable cause) {
        this(headers, new EmailNotConfirmedErrorBody(TYPE, DESCRIPTION), cause);
    }

    protected EmailNotConfirmedException(HttpHeaders headers, EmailNotConfirmedErrorBody body, Throwable cause) {
        super(headers, body, cause);
    }

    public static class EmailNotConfirmedErrorBody extends ForbiddenBody {

        protected EmailNotConfirmedErrorBody(String type, String description) {
            super(type, description);
        }
    }
}
