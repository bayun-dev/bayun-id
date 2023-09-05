export enum ErrorType {
    AUTH_RESTART = 'AUTH_RESTART',
    BAD_REQUEST = 'BAD_REQUEST',
    BAD_REQUEST_PARAMETERS = 'BAD_REQUEST_PARAMETERS',
    EMAIL_NOT_CONFIRMED = 'EMAIL_NOT_CONFIRMED',
    INTERNAL_ERROR = 'INTERNAL_ERROR',
    PASSWORD_INVALID = 'PASSWORD_INVALID',
    USERNAME_OCCUPIED = 'USERNAME_OCCUPIED',
    USERNAME_UNOCCUPIED = 'USERNAME_UNOCCUPIED',
}

type ErrorBody = {
    status: number,
    type: string,
    description: string,
    timestamp: number,
    parameters?: string[]
}

export default ErrorBody