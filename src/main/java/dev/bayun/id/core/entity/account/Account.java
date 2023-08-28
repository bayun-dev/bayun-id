package dev.bayun.id.core.entity.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "accounts")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Account implements UserDetails, Serializable {

    @Id
    private UUID id;

    private String username;

    @Column(name = "avatar_id")
    private UUID avatarId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name")),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name")),
            @AttributeOverride(name = "dateOfBirth", column = @Column(name = "date_of_birth")),
            @AttributeOverride(name = "gender", column = @Column(name = "gender"))
    })
    private Person person;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "email", column = @Column(name = "email")),
            @AttributeOverride(name = "emailConfirmed", column = @Column(name = "email_confirmed"))
    })
    private Contact contact;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lastModifiedDate", column = @Column(name = "last_modified_date"))
    })
    private Secret secret;

    @JsonIgnore
    @ElementCollection(targetClass = Authority.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "account_authorities", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    private Set<Authority> authorities;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "registrationDate", column = @Column(name = "registration_date"))
    })
    private Details details;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "deactivated", column = @Column(name = "deactivated")),
            @AttributeOverride(name = "reason", column = @Column(name = "deactivation_reason")),
            @AttributeOverride(name = "reasonMessage", column = @Column(name = "deactivation_reason_message")),
            @AttributeOverride(name = "date", column = @Column(name = "deactivation_date"))
    })
    private Deactivation deactivation;

    @Override
    @JsonIgnore
    public String getPassword() {
        return secret.getHash();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        if (deactivation.isDeactivated()) {
            return !deactivation.getReason().equals(Deactivation.Reason.BLOCKED);
        } else {
            return true;
        }
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        if (deactivation.isDeactivated()) {
            return !deactivation.getReason().equals(Deactivation.Reason.DELETED);
        } else {
            return true;
        }
    }
}
