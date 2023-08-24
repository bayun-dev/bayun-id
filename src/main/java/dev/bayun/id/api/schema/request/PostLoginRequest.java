package dev.bayun.id.api.schema.request;

import dev.bayun.id.core.validation.annotation.Password;
import dev.bayun.id.core.validation.annotation.Username;
import lombok.Data;

@Data
public class PostLoginRequest {

    @Username
    private String username;

    @Password
    private String password;

}
