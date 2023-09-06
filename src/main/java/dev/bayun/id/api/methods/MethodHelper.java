package dev.bayun.id.api.methods;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

@Component
@AllArgsConstructor
public class MethodHelper {

    public void checkBindingResult(BindingResult bindingResult) throws BindException {
        if (bindingResult != null && bindingResult.hasFieldErrors()) {
            throw new BindException(bindingResult);
        }
    }
}
