package dev.bayun.id.core.validation;

import dev.bayun.id.core.validation.annotation.Password;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    private boolean required;

    private Predicate<String> predicate;

    @Override
    public void initialize(Password annotation) {
        this.required = annotation.required();
        this.predicate = Pattern.compile(annotation.regexp()).asPredicate();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return !required;
        }

        return predicate.test(value);
    }

}
