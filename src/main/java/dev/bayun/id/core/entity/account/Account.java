package dev.bayun.id.core.entity.account;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "accounts")
public class Account implements UserDetails, Serializable {

    @Id
    @JsonValue
    private UUID id;

    private String username;

    private String firstName;

    private String lastName;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "avatar_id")
    private String avatarId;

    @Column(name = "email_id")
    private UUID emailId;

    private boolean blocked;

    private boolean deleted;

    @ElementCollection(targetClass = Authority.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "account_authorities", joinColumns = @JoinColumn(name = "account_id"))
    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    private Collection<Authority> authorities;

    @Override
    public String getPassword() {
        return getPasswordHash();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !blocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !deleted;
    }
}
