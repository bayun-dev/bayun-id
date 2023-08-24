package dev.bayun.id.core.validation;

import dev.bayun.id.core.validation.annotation.DateOfBirth;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateOfBirthValidator implements ConstraintValidator<DateOfBirth, String> {

    private boolean required;

    private DateFormat dateFormat;

    @Override
    public void initialize(DateOfBirth annotation) {
        this.required = annotation.required();
        this.dateFormat = new SimpleDateFormat(annotation.format());
        this.dateFormat.setLenient(false);
    }

    @Override
    public boolean isValid(String dateOfBirth, ConstraintValidatorContext context) {
        if (dateOfBirth == null) {
            return !required;
        }

        try {
            Date parse = dateFormat.parse(dateOfBirth);

            return true;
        } catch (ParseException e) {
            return false;
        }
    }

}
