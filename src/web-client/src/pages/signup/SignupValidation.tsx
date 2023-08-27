import {ReactNode} from "react";

type ValidationResponse = {
    error: string | undefined,
    help?: string | ReactNode | undefined
}

const usernameRegexp = new RegExp("^(?!.*_{2}.*)\\w{1,30}$")
export const validateUsername = (username: string): ValidationResponse => {
    if (username.length === 0) {
        return { error: 'enter a username' }
    }

    if (!usernameRegexp.test(username)) {
        return {
            error: 'invalid username',
            help: <>
                <ul className='list-disc list-inside'>
                    <li>
                        Use Latin letters, numbers and underlines.
                    </li>
                    <li>
                        Don't use two underlines in a row.
                    </li>
                    <li>
                        Up to 30 characters allowed.
                    </li>
                </ul>
            </>
        }
    }

    return { error: undefined }
}

const nameRegexp = new RegExp('^(?=.{1,30}$)(?!.*[ \\-.\',]{2})[a-zA-Z]+([a-zA-Z \\-.\',]*[a-zA-Z]+)*$')
const nameHelp = <>
    <ul className='list-disc list-inside'>
        <li>
            Use Latin and Russian letters.
        </li>
        <li>
            Use special characters: - ' , .
        </li>
        <li>
            Don't use two special characters in a row.
        </li>
        <li>
            Up to 30 characters allowed.
        </li>
    </ul>
</>
export const validateFirstname = (firstName: string): ValidationResponse => {
    if (firstName.length === 0) {
        return { error: 'enter your first name' }
    }

    if (!nameRegexp.test(firstName)) {
        return {
            error: 'invalid first name',
            help: nameHelp
        }
    }

    return { error: undefined }
}
export const validateLastname = (lastName: string): ValidationResponse => {
    if (lastName.length === 0) {
        return { error: 'enter your last name' }
    }

    if (!nameRegexp.test(lastName)) {
        return {
            error: 'invalid last name',
            help: nameHelp
        }
    }

    return { error: undefined }
}

const dateOfBirthRegexp = new RegExp('^\\d{2}[.]\\d{2}[.]\\d{4}$')
export const validateDateOfBirth = (dateOfBirth: Date): ValidationResponse => {
    return { error: undefined }
}

export const validateGender = (gender: string): ValidationResponse => {
    if (!(gender.toLowerCase() === 'male' || gender.toLowerCase() === 'female')) {
        return { error: 'invalid' }
    }

    return { error: undefined }
}

const emailRegexp = new RegExp('^(?=[\\S@]{3,320}$)\\S+@\\S+$')
export const validateEmail = (email: string): ValidationResponse => {
    if (email.length > 320) {
        return { error: 'sorry, this email is too long' }
    }

    if (!emailRegexp.test(email)) {
        return { error: 'invalid email' }
    }

    return { error: undefined }
}

const passwordRegexp = new RegExp('^(?=.{8,60}$).*$')
const passwordHelp = <>
    The password can contain any characters and must be between 8 and 60 characters long.
</>
export const validatePassword = (password: string): ValidationResponse => {
    if (password.length === 0) {
        return { error: 'enter a password' }
    }

    if (!passwordRegexp.test(password)) {
        return { error: 'invalid password', help: passwordHelp }
    }

    return { error: undefined }
}