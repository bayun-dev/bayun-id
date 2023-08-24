package dev.bayun.id.core.entity.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Data
@ToString(exclude = {"hash"})
@Embeddable
public class Secret implements Serializable {

    @JsonIgnore
    private String hash;

    private long lastModifiedDate;

}
