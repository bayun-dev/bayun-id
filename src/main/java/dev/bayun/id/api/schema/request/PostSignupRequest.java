package dev.bayun.id.api.schema.request;

import dev.bayun.id.core.validation.annotation.*;
import lombok.Data;

@Data
public class PostSignupRequest {

    @Username
    private String username;

    @FirstName
    private String firstName;

    @LastName
    private String lastName;

    @DateOfBirth
    private String dateOfBirth;

    @Gender
    private String gender;

    @Password
    private String password;

    @Email(required = false)
    private String email;

}
