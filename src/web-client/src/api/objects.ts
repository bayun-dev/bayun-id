/**
 * Account
 */
export type Account = {
    id?: string
    username?: string
    person?: Person
    avatar?: Avatar | undefined
    contact?: Contact
    password?: Password
    deactivation?: Deactivation
    details?: Details

    // registrationDate: number // unix-time
    // deactivation: Deactivation
}

export type Person = {
    firstName?: string
    lastName?: string
    dateOfBirth?: string // format: dd.mm.yyyy
    gender?: Gender
}

export enum Gender {
    MALE = "male",
    FEMALE = "female"
}

export type Avatar = { // several copies of different sizes by url-param
    id?: number // avatarId
}

export type Contact = {
    email?: string | undefined
    emailConfirmed?: boolean | undefined
}

export type Password = {
    raw?: string
    lastModifiedDate?: number
}

export type Deactivation = {
    deactivated?: boolean
    date?: number | undefined
    reason?: string | undefined
}

export type Details = {
    registrationDate?: number
}

/**
 * Authentication
 */
export type Authentication = {
    accountId: string
    username: string
}

export type AuthenticationContext = {
    currentAccountId: string | undefined,
    authorizedAccounts: AuthorizedAccount[]
}

export type AuthorizedAccount = {
    id: number
    username: string
    firstName: string
    lastName: string
    avatarId: string
}