package dev.bayun.id.core.modal;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Data
public class AccountUpdateToken {

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private String gender;

    private String email;

    private String password;

    private MultipartFile avatar;

}
