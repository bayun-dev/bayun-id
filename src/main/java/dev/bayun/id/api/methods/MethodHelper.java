package dev.bayun.id.api.methods;

import dev.bayun.id.core.exception.BadRequestParametersException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
@AllArgsConstructor
public class MethodHelper {

    public void checkBindingResult(BindingResult bindingResult) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new BadRequestParametersException(bindingResult.getFieldErrors()
                    .stream().map(FieldError::getField)
                    .toList().toArray(String[]::new));
        }
    }
}
