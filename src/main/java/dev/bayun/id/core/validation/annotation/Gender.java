package dev.bayun.id.core.validation.annotation;

import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.core.validation.GenderValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = GenderValidator.class)
public @interface Gender {

    String message() default Errors.GENDER_INVALID_CODE;

    boolean required() default true;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}