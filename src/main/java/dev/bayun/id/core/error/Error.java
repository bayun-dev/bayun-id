package dev.bayun.id.core.error;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Error {
    ACCESS_DENIED(403, "Access denied."),
    ACCOUNT_BLOCKED(403, "Account blocked."),
    ACCOUNT_DELETED(403, "Account deleted."),
    AUTH_RESTART(403, "Restart the authorization process."),
    EMAIL_NOT_CONFIRMED(403, "Email not specified or not confirmed."),
    INTERNAL(500, "Internal server error."),
    INVALID_REQUEST(400, "The request is not valid."),
    INVALID_REQUEST_PARAM(400, "One of the parameters specified was missing or not valid."),
    PASSWORD_INVALID(400, "The provided password is not valid."),
    USERNAME_OCCUPIED(400, "The username is already in use."),
    USERNAME_UNOCCUPIED(400, "The username is not yet being used.");

    private final int status;

    private final String description;

    Error(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public String getType() {
        return this.name();
    }
}
