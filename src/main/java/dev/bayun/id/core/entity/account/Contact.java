package dev.bayun.id.core.entity.account;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class Contact implements Serializable {

    private String email;

    private Boolean emailConfirmed;

}