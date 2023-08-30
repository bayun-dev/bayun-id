package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseErrorResponseException {

    public NotFoundException() {
        this(HttpHeaders.EMPTY, null);
    }

    public NotFoundException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public NotFoundException(HttpHeaders headers, Throwable cause) {
        this(headers,
                new NotFoundErrorBody(BaseErrorType.NOT_FOUND.getType(),
                        BaseErrorType.NOT_FOUND.getDescription()),
                cause);
    }

    protected NotFoundException(HttpHeaders headers, NotFoundErrorBody body, Throwable cause) {
        super(HttpStatus.NOT_FOUND.value(), headers, body, cause);
    }

    public static class NotFoundErrorBody extends BaseErrorBody {

        protected NotFoundErrorBody(String type, String description) {
            super(HttpStatus.NOT_FOUND.value(), type, description);
        }

    }

}
