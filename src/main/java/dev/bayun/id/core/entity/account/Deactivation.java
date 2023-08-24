package dev.bayun.id.core.entity.account;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Embeddable
public class Deactivation implements Serializable {

    private boolean deactivated;

    @Enumerated(EnumType.STRING)
    private Deactivation.Reason reason;

    private String reasonMessage;

    private Long date;

    public enum Reason {
        BLOCKED,
        DELETED
    }

}
