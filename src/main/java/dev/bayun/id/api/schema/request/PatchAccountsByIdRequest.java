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
    @FirstName(required = false)
    public String getFirstName() {
        return super.getFirstName();
    }

    @Override
    @LastName(required = false)
    public String getLastName() {
        return super.getLastName();
    }

    @Override
    @DateOfBirth(required = false)
    public String getDateOfBirth() {
        return super.getDateOfBirth();
    }

    @Override
    @Gender(required = false)
    public String getGender() {
        return super.getGender();
    }

    @Override
    @Email(required = false)
    public String getEmail() {
        return super.getEmail();
    }

    @Override
    @Password(required = false)
    public String getPassword() {
        return super.getPassword();
    }
}
