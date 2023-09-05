package dev.bayun.id.core.entity.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "account_emails")
public class Email {

    @Id
    private UUID id;

    private String email;

    private boolean confirmed;

}
