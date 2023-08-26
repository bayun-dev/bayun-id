package dev.bayun.id.core.entity.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Data
@ToString(exclude = {"hash"})
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Secret implements Serializable {

    @JsonIgnore
    private String hash;

    private Long lastModifiedDate;

}
