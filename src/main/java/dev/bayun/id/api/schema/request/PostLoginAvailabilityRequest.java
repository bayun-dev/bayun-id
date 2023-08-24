package dev.bayun.id.api.schema.request;

import dev.bayun.id.core.validation.annotation.Username;
import lombok.Data;

@Data
public class PostLoginAvailabilityRequest {

    @Username
    private String username;

}
