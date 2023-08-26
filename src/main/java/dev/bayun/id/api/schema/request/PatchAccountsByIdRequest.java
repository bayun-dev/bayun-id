package dev.bayun.id.api.schema.request;

import dev.bayun.id.core.modal.AccountUpdateToken;
import dev.bayun.id.core.validation.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PatchAccountsByIdRequest extends AccountUpdateToken {

    @Override
    @FirstName
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    @LastName
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    @DateOfBirth
    public String getDateOfBirth() {
        return super.getDateOfBirth();
    }

    @Override
    @Gender
    public String getGender() {
        return super.getGender();
    }

    @Override
    @Email
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    @Password
    public String getPassword() {
        return super.getPassword();
    }
}
