package dev.bayun.id.core.entity.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "email_update_tokens")
public class EmailUpdateToken {

    @Id
    private UUID id;

    private UUID accountId;

    private String email;

    private long date;

}
