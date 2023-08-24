package dev.bayun.id.core.validation;

import dev.bayun.id.core.validation.annotation.FirstName;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public class FirstNameValidator implements ConstraintValidator<FirstName, String> {

    private boolean required;

    private Predicate<String> predicate;

    @Override
    public void initialize(FirstName annotation) {
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
