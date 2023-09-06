package dev.bayun.id.util;

import dev.bayun.id.core.entity.account.Account;
import dev.bayun.id.core.entity.account.Authority;

import java.util.Set;
import java.util.UUID;

public class TestAccountBuilder {

    private UUID id;

    private String username;

    private Set<Authority> authorities;

    private String avatarId;

    private String firstName;

    private String lastName;

    private String passwordHash;

    private UUID emailId;

    private boolean blocked;

    private boolean deleted;

    public TestAccountBuilder() {

    }

    public TestAccountBuilder id(UUID id) {
        this.id = id;
        return this;
    }

    public TestAccountBuilder username(String username) {
        this.username = username;
        return this;
    }

    public TestAccountBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public TestAccountBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public TestAccountBuilder avatarId(String avatarId) {
        this.avatarId = avatarId;
        return this;
    }

    public TestAccountBuilder emailId(UUID emailId) {
        this.emailId = emailId;
        return this;
    }

    public TestAccountBuilder passwordHash(String passwordHash) {
        this.passwordHash = passwordHash;
        return this;
    }

    public TestAccountBuilder authorities(Set<Authority> authorities) {
        this.authorities = authorities;
        return this;
    }

    public Account build() {
        Account account = new Account();
        account.setId(this.id);
        account.setUsername(this.username);
        account.setAuthorities(authorities);

        account.setFirstName(this.firstName);
        account.setLastName(this.lastName);
        account.setEmailId(this.emailId);
        account.setAvatarId(this.avatarId);
        account.setPasswordHash(this.passwordHash);

        return account;
    }
}
