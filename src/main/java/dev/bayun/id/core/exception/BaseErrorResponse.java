package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;

public interface BaseErrorResponse {

    int getStatus();

    HttpHeaders getHeaders();

    BaseErrorBody getBody();

}
