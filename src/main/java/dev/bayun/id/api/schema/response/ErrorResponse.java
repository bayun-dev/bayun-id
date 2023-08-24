package dev.bayun.id.api.schema.response;

import dev.bayun.id.api.schema.Error;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse extends AbstractResponse {

    private Collection<Error> errors;

    public ErrorResponse(Error... errors) {
        this(Arrays.asList(errors));
    }

    public ErrorResponse(Collection<Error> errors) {
        setErrors(new HashSet<>(errors));
    }
}
