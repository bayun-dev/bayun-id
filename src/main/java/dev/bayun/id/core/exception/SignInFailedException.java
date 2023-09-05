package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;

public class SignInFailedException extends InternalErrorException {

    public static final String TYPE = "SIGN_IN_FAILED";
    public static final String DESCRIPTION = "Failure while signing in.";

    public SignInFailedException() {
        this(HttpHeaders.EMPTY, null);
    }

    public SignInFailedException(HttpHeaders headers) {
        this(headers, null);
    }

    public SignInFailedException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public SignInFailedException(HttpHeaders headers, Throwable cause) {
        this(headers, new SignInFailedBody(TYPE, DESCRIPTION), cause);
    }

    protected SignInFailedException(HttpHeaders headers, SignInFailedBody body, Throwable cause) {
        super(headers, body, cause);
    }

    public static class SignInFailedBody extends InternalErrorBody {

        protected SignInFailedBody(String type, String description) {
            super(type, description);
        }
    }

}
