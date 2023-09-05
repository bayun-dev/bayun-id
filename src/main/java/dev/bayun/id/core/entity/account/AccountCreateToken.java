package dev.bayun.id.core.entity.account;

import dev.bayun.id.core.validation.annotation.FirstName;
import dev.bayun.id.core.validation.annotation.LastName;
import dev.bayun.id.core.validation.annotation.Password;
import dev.bayun.id.core.validation.annotation.Username;
import lombok.Data;

@Data
public class AccountCreateToken {

    @Username
    private String username;

    @FirstName
    private String firstName;

    @LastName
    private String lastName;

    @Password
    private String password;

}
