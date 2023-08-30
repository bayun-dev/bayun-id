package dev.bayun.id.core.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.web.ErrorResponse;
import org.springframework.web.ErrorResponseException;

import java.util.Objects;

@Getter
public class BaseErrorResponseException extends RuntimeException implements BaseErrorResponse {

    private final int status;

    private final BaseErrorBody body;

    private final HttpHeaders headers;

    public BaseErrorResponseException(ErrorResponse errorResponse) {
        this(errorResponse.getStatusCode().value(),
                errorResponse.getHeaders(),
                BaseErrorBody.of(errorResponse.getBody()),
                errorResponse instanceof ErrorResponseException theEx ? theEx.getCause() : null);
    }

    public BaseErrorResponseException(int status, HttpHeaders headers, BaseErrorBody body) {
        this(status, headers, body, null);
    }

    public BaseErrorResponseException(int status, HttpHeaders headers, BaseErrorBody body, Throwable cause) {
        super(Objects.nonNull(body) ? body.getType() + ": " + body.getDescription() : null, cause);
        this.status = status;
        this.headers = Objects.requireNonNullElse(headers, HttpHeaders.EMPTY);
        this.body = body;
    }
}
