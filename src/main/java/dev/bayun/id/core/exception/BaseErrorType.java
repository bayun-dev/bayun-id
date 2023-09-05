package dev.bayun.id.core.exception;

import lombok.Getter;
import org.springframework.http.ProblemDetail;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

public enum BaseErrorType {
    UNKNOWN("Unknown error occurred."),
    BAD_REQUEST(""),
    BAD_REQUEST_PARAMETERS(""),
    NOT_FOUND("Not found."),
    ACCOUNT_NOT_FOUND("Account not found.");

    private final String description;

    BaseErrorType(String description) {
        this.description = description;
    }

    public String getType() {
        return this.name();
    }

    public String getDescription() {
        return this.description;
    }
}
