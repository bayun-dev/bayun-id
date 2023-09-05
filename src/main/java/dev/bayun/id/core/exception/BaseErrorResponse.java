package dev.bayun.id.core.exception;

import dev.bayun.id.api.schema.BaseResponse;
import org.springframework.http.HttpHeaders;

public interface BaseErrorResponse extends BaseResponse {

    int getStatus();

    HttpHeaders getHeaders();

    BaseErrorBody getBody();

}
