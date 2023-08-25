package dev.bayun.id.util;

import dev.bayun.id.core.entity.account.*;

import java.util.Set;
import java.util.UUID;

public class TestAccountBuilder {

    private UUID id;

    private String username;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    private Person.Gender gender;

    private String email;

    private Boolean emailConfirmed;

    private long registrationDate;

    private String secretHash;

    private long secretLastModified;

    private boolean deactivated;

    private Deactivation.Reason deactivationReason;

    private Long deactivationDate;

    private Set<Authority> authorities;

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

    public TestAccountBuilder dateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public TestAccountBuilder gender(Person.Gender gender) {
        this.gender = gender;
        return this;
    }

    public TestAccountBuilder email(String email) {
        this.email = email;
        return this;
    }

    public TestAccountBuilder emailConfirmed(Boolean emailConfirmed) {
        this.emailConfirmed = emailConfirmed;
        return this;
    }

    public TestAccountBuilder registrationDate(long registrationDate) {
        this.registrationDate = registrationDate;
        return this;
    }

    public TestAccountBuilder secretHash(String secretHash) {
        this.secretHash = secretHash;
        return this;
    }

    public TestAccountBuilder secretLastModified(long secretLastModified) {
        this.secretLastModified = secretLastModified;
        return this;
    }

    public TestAccountBuilder deactivated(boolean deactivated) {
        this.deactivated = deactivated;
        return this;
    }

    public TestAccountBuilder deactivationReason(Deactivation.Reason deactivationReason) {
        this.deactivationReason = deactivationReason;
        return this;
    }

    public TestAccountBuilder deactivationDate(Long deactivationDate) {
        this.deactivationDate = deactivationDate;
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

        Person person = new Person();
        person.setFirstName(this.firstName);
        person.setLastName(this.lastName);
        person.setDateOfBirth(this.dateOfBirth);
        person.setGender(this.gender);
        account.setPerson(person);

        Contact contact = new Contact();
        contact.setEmail(this.email);
        contact.setEmailConfirmed(this.emailConfirmed);
        account.setContact(contact);

        Details details = new Details();
        details.setRegistrationDate(this.registrationDate);
        account.setDetails(details);

        Deactivation deactivation = new Deactivation();
        deactivation.setDeactivated(this.deactivated);
        deactivation.setReason(this.deactivationReason);
        deactivation.setDate(this.deactivationDate);
        account.setDeactivation(deactivation);

        Secret secret = new Secret();
        secret.setHash(this.secretHash);
        secret.setLastModifiedDate(this.secretLastModified);
        account.setSecret(secret);

        account.setAuthorities(authorities);

        return account;
    }
}
