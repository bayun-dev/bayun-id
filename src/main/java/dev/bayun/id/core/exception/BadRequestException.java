package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseErrorResponseException {

    public BadRequestException() {
        this(null);
    }

    public BadRequestException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public BadRequestException(HttpHeaders headers, Throwable cause) {
        this(headers,
                new BadRequestErrorBody(BaseErrorType.BAD_REQUEST.getType(),
                        BaseErrorType.BAD_REQUEST.getDescription()),
                cause);
    }

    protected BadRequestException(HttpHeaders headers, BadRequestErrorBody body, Throwable cause) {
        super(HttpStatus.BAD_REQUEST.value(), headers, body, cause);
    }

    public static class BadRequestErrorBody extends BaseErrorBody {

        protected BadRequestErrorBody(String type, String description) {
            super(HttpStatus.BAD_REQUEST.value(), type, description);
        }

    }

}
