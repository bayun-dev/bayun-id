package dev.bayun.id.core.service.email;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class EmailContext {

    @Getter
    private final String subject;

    @Getter
    private final String templateLocation;

    @Getter
    private final Map<String, Object> variables;

    public EmailContext(String subject, String templateLocation) {
        this.subject = subject;
        this.templateLocation = templateLocation;
        this.variables = new HashMap<>();
    }
}
