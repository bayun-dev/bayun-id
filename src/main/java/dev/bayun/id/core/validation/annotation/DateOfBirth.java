package dev.bayun.id.core.validation.annotation;

import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.core.validation.DateOfBirthValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DateOfBirthValidator.class)
public @interface DateOfBirth {

    String DEFAULT_FORMAT = "dd.MM.yyyy";

    String message() default Errors.DATE_OF_BIRTH_INVALID_CODE;

    String format() default "dd.MM.yyyy";

    boolean required() default true;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}