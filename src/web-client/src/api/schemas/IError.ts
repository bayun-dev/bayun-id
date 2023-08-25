type IError = {
    code: string
    description: string
}

export enum IErrorCode {
    ACCOUNT_BLOCKED = 'ACCOUNT_BLOCKED',
    ACCOUNT_DELETED = 'ACCOUNT_DELETED',
    USERNAME_INVALID = 'USERNAME_INVALID',
    USERNAME_NOT_OCCUPIED = 'USERNAME_NOT_OCCUPIED',
    PASSWORD_INVALID = 'PASSWORD_INVALID',
    CREDENTIALS_NOT_FOUND = 'CREDENTIALS_NOT_FOUND',
}

export default IError