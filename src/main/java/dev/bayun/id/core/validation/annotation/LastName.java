package dev.bayun.id.core.validation.annotation;

import dev.bayun.id.api.schema.Errors;
import dev.bayun.id.core.validation.LastNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = LastNameValidator.class)
public @interface LastName {

    String DEFAULT_REGEXP = "^(?=.{1,30}$)(?!.*[ \\-.',]{2})[a-zA-Z]+([a-zA-Z \\-.',]*[a-zA-Z]+)*$";

    String message() default Errors.LASTNAME_INVALID_CODE;

    boolean required() default true;

    String regexp() default DEFAULT_REGEXP;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
