type Account = {
    id: string
    username: string
    person?: {
        firstName: string
        lastName: string
        dateOfBirth: string
        gender: string // male or female
    }
    contact?: {
        email?: string,
        emailConfirmed?: boolean
    }
    secret?: {
        lastModifiedDate: number
    }
    details: {
        registrationDate: number
    }
    deactivation: {
        deactivated: boolean
        reason?: string
        date?: number
    }
}


export default Account