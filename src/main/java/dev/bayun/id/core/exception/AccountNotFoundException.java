package dev.bayun.id.core.exception;

import org.springframework.http.HttpHeaders;

public class AccountNotFoundException extends NotFoundException {

    public AccountNotFoundException() {
        this(HttpHeaders.EMPTY, null);
    }

    public AccountNotFoundException(Throwable cause) {
        this(HttpHeaders.EMPTY, cause);
    }

    public AccountNotFoundException(HttpHeaders headers, Throwable cause) {
        this(headers,
                new AccountNotFoundErrorBody(BaseErrorType.ACCOUNT_NOT_FOUND.getType(),
                        BaseErrorType.ACCOUNT_NOT_FOUND.getDescription()),
                cause);
    }

    protected AccountNotFoundException(HttpHeaders headers, AccountNotFoundErrorBody body, Throwable cause) {
        super(headers, body, cause);
    }

    public static class AccountNotFoundErrorBody extends NotFoundErrorBody {

        protected AccountNotFoundErrorBody(String type, String description) {
            super(type, description);
        }
    }
}
