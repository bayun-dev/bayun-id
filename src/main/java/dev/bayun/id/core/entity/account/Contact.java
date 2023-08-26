package dev.bayun.id.core.entity.account;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Contact implements Serializable {

    private String email;

    private Boolean emailConfirmed;

}