import * as React from "react";
import {ChangeEvent, useState} from "react";
import InputText from "../component/InputText";
import Checkbox from "../component/Checkbox";
import * as UsernameValidator from "../api/validation/UsernameValidator"
import * as NameValidator from "../api/validation/NameValidator"
import * as PasswordValidator from "../api/validation/PasswordValidator"
import * as EmailValidator from "../api/validation/EmailValidator"
import MethodAuthSignUp from "../api/methods/MethodAuthSignUp";
import {AxiosError} from "axios";
import ErrorBody, {ErrorType} from "../api/objects/ErrorBody";
import useErrorPage from "../hooks/useErrorPage";
import Header from "../component/Header";
import Logo from "../component/Logo";

const SignupForm = () => {

    const errorPage = useErrorPage()

    const [username, setUsername] = useState<string>('')
    const [usernameError, setUsernameError] = useState<string | undefined>(undefined)
    function onChangeUsername(e: ChangeEvent<HTMLInputElement>) {
        const value = e.currentTarget.value.trim()
        setUsername(value)
        setUsernameError(UsernameValidator.inlineValidate(value))
    }

    const [firstName, setFirstName] = useState<string>('')
    const [firstNameError, setFirstNameError] = useState<string | undefined>(undefined)
    function onChangeFirstName(e: ChangeEvent<HTMLInputElement>) {
        const value = e.currentTarget.value
        setFirstName(value)
        setFirstNameError(NameValidator.inlineValidate(value))
    }

    const [lastName, setLastName] = useState<string>('')
    const [lastNameError, setLastNameError] = useState<string | undefined>(undefined)
    function onChangeLastName(e: ChangeEvent<HTMLInputElement>) {
        const value = e.currentTarget.value
        setLastName(value)
        setLastNameError(NameValidator.inlineValidate(value))
    }

    const [email, setEmail] = useState<string>('')
    const [emailError, setEmailError] = useState<string | undefined>(undefined)
    function onChangeEmail(e: ChangeEvent<HTMLInputElement>) {
        const value = e.currentTarget.value.trim()
        setEmail(value)
        setEmailError(undefined)
    }

    const [password, setPassword] = useState<string>('')
    const [passwordError, setPasswordError] = useState<string | undefined>(undefined)
    const [hidePassword, setHidePassword] = useState<boolean>(false)
    function onChangePassword(e: ChangeEvent<HTMLInputElement>) {
        const value = e.currentTarget.value
        setPassword(value)
        setPasswordError(PasswordValidator.inlineValidate(value))
    }

    async function onSignUp() {
        const usernameValidationResult = UsernameValidator.fullValidate(username);
        const firstNameValidationResult = NameValidator.fullValidate(firstName);
        const lastNameValidationResult = NameValidator.fullValidate(lastName);
        const emailValidationResult = EmailValidator.quicklyValidate(email);
        const passwordValidationResult = PasswordValidator.fullValidate(password);

        if (usernameValidationResult || firstNameValidationResult || lastNameValidationResult || emailValidationResult || passwordValidationResult) {
            setUsernameError(usernameValidationResult)
            setFirstNameError(firstNameValidationResult)
            setLastNameError(lastNameValidationResult)
            setEmailError(emailValidationResult)
            setPasswordError(passwordValidationResult)
            return
        }

        const method = new MethodAuthSignUp({username, firstName, lastName, password, email})
        method.execute().then(r => {
            if (r.data.ok) {
                window.location.replace('/login')
            } else {
                throw ErrorType.INTERNAL
            }
        }).catch((e: AxiosError<ErrorBody>) => {
            if (!(e.response && e.response.data)) {
                errorPage.show.internalError()
                return
            }

            const errorBody = e.response.data
            if (errorBody.type === ErrorType.INVALID_REQUEST_PARAM) {
                errorBody.parameters?.forEach(parameter => {
                    if (parameter === 'username') {
                        setUsernameError('Invalid')
                    } else if (parameter === 'firstName') {
                        setFirstNameError('Invalid')
                    } else if (parameter === 'lastName') {
                        setLastNameError('Invalid')
                    } else if (parameter === 'email') {
                        setEmailError('Invalid')
                    } else if (parameter === 'password') {
                        setPasswordError('Invalid')
                    } else {
                        // ignore
                    }
                })
            } else if (errorBody.type === ErrorType.USERNAME_OCCUPIED) {
                setUsernameError('Already taken')
            } else {
                errorPage.show.internalError()
            }
        })
    }

    return <>
        <div className='w-full xs:w-xs p-4 space-y-8'>
            <div className='text-center'>
                <span className='text-3xl font-medium'>Registration</span>
            </div>
            <InputText placeholder='Username' value={username} onChange={onChangeUsername} errorMessage={usernameError} autoFocus={true}/>
            <div className='grid grid-cols-1 xs:grid-cols-2 gap-8 xs:gap-4'>
                <InputText placeholder='First name' value={firstName} onChange={onChangeFirstName} errorMessage={firstNameError}/>
                <InputText placeholder='Last name' value={lastName} onChange={onChangeLastName} errorMessage={lastNameError}/>
            </div>
            <div className='space-y-4'>
                <InputText placeholder='Password' value={password} onChange={onChangePassword} errorMessage={passwordError} type={hidePassword ? 'password' : 'text'}/>
                <Checkbox color='dark' value={hidePassword} label='Hide password' onChange={e => setHidePassword(e.target.checked)}/>
            </div>
            <InputText placeholder='Email' value={email} onChange={onChangeEmail} errorMessage={emailError}>
                Email is required for account recovery.
            </InputText>
            <button className='btn-dark w-full text-xl' onClick={onSignUp}>Sign Up</button>
        </div>
    </>
}

const SignupPageHeader = () => {
    return <Header>
        <a className='flex h-full items-center font-logo text-3xl' href='/'>
            <Logo/>
        </a>
        <div className='h-full flex justify-end items-center'>
            <a className='btn-dark-outline' href='/login'>Log In</a>
        </div>
    </Header>
}

const SignupPage = () => {
    return <>
        <div className='flex flex-col h-full'>
            <SignupPageHeader/>
            <main className='flex flex-col justify-center items-center grow'>
                <SignupForm/>
            </main>
        </div>
    </>
}

export default SignupPage