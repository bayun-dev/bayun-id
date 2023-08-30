package dev.bayun.id.core.exception;

import lombok.Getter;
import org.springframework.http.ProblemDetail;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class BaseErrorBody {

    private final int status;

    private final long timestamp;

    private final String type;

    private final String description;

    private final Map<String, Object> properties;

    public BaseErrorBody(int status, String type, String description) {
        this(status, type, description, System.currentTimeMillis());
    }

    public BaseErrorBody(int status, String type, String description, long timestamp) {
        Assert.notNull(type, "The type must not be null");
        Assert.notNull(description, "The description must not be null");

        this.status = status;
        this.type = type;
        this.description = description;
        this.timestamp = timestamp;
        this.properties = new LinkedHashMap<>();
    }

    public static BaseErrorBody of(ProblemDetail problemDetail) {
        return new BaseErrorBody(problemDetail.getStatus(), problemDetail.getTitle(), problemDetail.getDetail());
    }

    protected void setProperty(String key, Object value) {
        this.properties.put(key, value);
    }
}
