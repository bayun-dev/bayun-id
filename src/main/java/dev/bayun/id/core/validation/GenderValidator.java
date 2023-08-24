package dev.bayun.id.core.validation;

import dev.bayun.id.core.entity.account.Person;
import dev.bayun.id.core.validation.annotation.Gender;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<Gender, String> {

    private boolean required;

    @Override
    public void initialize(Gender annotation) {
        this.required = annotation.required();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return !required;
        }

        try {
            Person.Gender.fromValue(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
