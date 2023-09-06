package dev.bayun.id.core.entity.account;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public enum Authority implements GrantedAuthority, Serializable {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_BLOCKED,
    ROLE_DELETED;

    @Override
    public String getAuthority() {
        return this.name();
    }

}
