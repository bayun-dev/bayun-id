package dev.bayun.id.core.entity.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.io.Serializable;

@Data
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Deactivation implements Serializable {

    private boolean deactivated;

    @Enumerated(EnumType.STRING)
    private Deactivation.Reason reason;

    private Long date;

    public enum Reason {
        BLOCKED,
        DELETED
    }

}
