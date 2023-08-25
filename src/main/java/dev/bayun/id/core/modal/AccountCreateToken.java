package dev.bayun.id.core.modal;

import lombok.Data;

@Data
public class AccountCreateToken {

    private String username;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String gender;

    private String password;

    private String email;

}
