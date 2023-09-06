export enum ErrorType {
    ACCESS_DENIED = 'ACCESS_DENIED',
    ACCOUNT_BLOCKED = 'ACCOUNT_BLOCKED',
    ACCOUNT_DELETED = 'ACCOUNT_DELETED',
    AUTH_RESTART = 'AUTH_RESTART',
    EMAIL_NOT_CONFIRMED = 'EMAIL_NOT_CONFIRMED',
    INTERNAL = 'INTERNAL',
    INVALID_REQUEST = 'INVALID_REQUEST',
    INVALID_REQUEST_PARAM = 'INVALID_REQUEST_PARAM',
    PASSWORD_INVALID = 'PASSWORD_INVALID',
    USERNAME_OCCUPIED = 'USERNAME_OCCUPIED',
    USERNAME_UNOCCUPIED = 'USERNAME_UNOCCUPIED'
}

type ErrorBody = {
    status: number,
    type: string,
    description: string,
    timestamp: number,
    parameters?: string[]
}

export default ErrorBody