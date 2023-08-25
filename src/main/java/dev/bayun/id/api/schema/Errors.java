package dev.bayun.id.api.schema;

import java.lang.reflect.Field;

public class Errors {


    public static final String ACCOUNT_BLOCKED_CODE = "ACCOUNT_BLOCKED";

    public static final Error ACCOUNT_BLOCKED = new Error(ACCOUNT_BLOCKED_CODE, "Account blocked.");

    public static final String ACCOUNT_DELETED_CODE = "ACCOUNT_DELETED";

    public static final Error ACCOUNT_DELETED = new Error(ACCOUNT_DELETED_CODE, "Account deleted.");

    public static final String ACCOUNT_NOT_FOUND_CODE = "ACCOUNT_NOT_FOUND";

    public static final Error ACCOUNT_NOT_FOUND = new Error(ACCOUNT_NOT_FOUND_CODE, "Account not found.");

    public static final String CREDENTIALS_NOT_FOUND_CODE = "CREDENTIALS_NOT_FOUND";

    public static final Error CREDENTIALS_NOT_FOUND = new Error(CREDENTIALS_NOT_FOUND_CODE, "The provided credentials is bad");

    public static final String DATE_OF_BIRTH_INVALID_CODE = "DATE_OF_BIRTH_INVALID";

    public static final Error DATE_OF_BIRTH_INVALID = new Error(DATE_OF_BIRTH_INVALID_CODE, "The date of birth is invalid.");

    public static final String EMAIL_INVALID_CODE = "EMAIL_INVALID";

    public static final Error EMAIL_INVALID = new Error("EMAIL_INVALID", "The email is invalid.");

    public static final String FIRSTNAME_INVALID_CODE = "FIRSTNAME_INVALID";

    public static final Error FIRSTNAME_INVALID = new Error(FIRSTNAME_INVALID_CODE, "The first name is invalid.");

    public static final String GENDER_INVALID_CODE = "GENDER_INVALID";

    public static final Error GENDER_INVALID = new Error(GENDER_INVALID_CODE, "The gender is invalid.");

    public static final String LASTNAME_INVALID_CODE = "LASTNAME_INVALID";

    public static final Error LASTNAME_INVALID = new Error(LASTNAME_INVALID_CODE, "The last name is invalid.");

    public static final String PASSWORD_INVALID_CODE = "PASSWORD_INVALID";

    public static final Error PASSWORD_INVALID = new Error(PASSWORD_INVALID_CODE, "The provided password is invalid.");

    public static final String USERNAME_INVALID_CODE = "USERNAME_INVALID";

    public static final Error USERNAME_INVALID = new Error(USERNAME_INVALID_CODE, "The provided username is not valid.");

    public static final String USERNAME_NOT_OCCUPIED_CODE = "USERNAME_NOT_OCCUPIED";

    public static final Error USERNAME_NOT_OCCUPIED = new Error(USERNAME_NOT_OCCUPIED_CODE, "The provided username is not occupied.");

    public static final String USERNAME_OCCUPIED_CODE = "USERNAME_OCCUPIED";

    public static final Error USERNAME_OCCUPIED = new Error(USERNAME_OCCUPIED_CODE, "The provided username is already occupied.");

    public static Error getByCode(String code) {
        if (code == null) {
            throw new NullPointerException();
        }

        try {
            Field errorField = Error.class.getField(code);
            return (Error) errorField.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }

}
