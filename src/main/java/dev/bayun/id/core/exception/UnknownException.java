package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

public class UnknownException extends BaseErrorResponseException {

    public UnknownException() {
        this(HttpHeaders.EMPTY, null);
    }

    public UnknownException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public UnknownException(HttpHeaders headers, Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                headers,
                new BaseErrorBody(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        BaseErrorType.UNKNOWN.getType(),
                        BaseErrorType.UNKNOWN.getDescription()),
                cause);
    }
}
