import * as React from "react";
import {ChangeEvent, useState} from "react";
import InputText from "../component/InputText";
import Checkbox from "../component/Checkbox";
import MethodAuthSignIn from "../api/methods/MethodAuthSignIn";
import * as UsernameValidator from "../api/validation/UsernameValidator"
import * as PasswordValidator from "../api/validation/PasswordValidator"
import {AxiosError} from "axios";
import ErrorBody, {ErrorType} from "../api/objects/ErrorBody";
import useErrorPage from "../hooks/useErrorPage";
import Logo from "../component/Logo";
import Header from "../component/Header";

const LoginForm = () => {

    const errorPage = useErrorPage()

    const [username, setUsername] = useState<string>('')
    const [usernameError, setUsernameError] = useState<string | undefined>(undefined)
    const onChangeUsername = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.currentTarget.value.trim()
        setUsername(value)
        setUsernameError(undefined)
    }

    const [password, setPassword] = useState<string>('')
    const [passwordError, setPasswordError] = useState<string | undefined>(undefined)
    const [hidePassword, setHidePassword] = useState<boolean>(false)
    const onChangePassword = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.currentTarget.value
        setPassword(value)
        setPasswordError(undefined)
    }

    async function onLogin() {
        const usernameValidationResult = UsernameValidator.quicklyValidate(username);
        const passwordValidationResult = PasswordValidator.quicklyValidate(password);
        if (usernameValidationResult || passwordValidationResult) {
            setUsernameError(usernameValidationResult)
            setPasswordError(passwordValidationResult)
            return
        }

        const method = new MethodAuthSignIn({username, password})
        method.execute().then(r => {
            if (r.data.ok) {
                window.location.replace(r.data.redirectUri)
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
                    } else if (parameter === 'password') {
                        setPasswordError('Invalid')
                    } else {
                        // ignore
                    }
                })
            } else if (errorBody.type === ErrorType.USERNAME_UNOCCUPIED) {
                setUsernameError('Account doesn\'t exist')
            } else if (errorBody.type === ErrorType.PASSWORD_INVALID) {
                setPasswordError('Incorrect')
            } else {
                errorPage.show.internalError()
            }
        })
    }

    return <>
        <div className='w-full xs:w-xs p-4 space-y-8'>
            <div className='text-center'>
                <span className='text-3xl font-medium'>Authorization</span>
            </div>
            <div className='space-y-4'>
                <InputText placeholder='Username' autoFocus={true}
                           value={username} onChange={onChangeUsername}
                           errorMessage={usernameError} />
                <InputText placeholder='Password'
                           type={hidePassword ? 'password' : 'text'}
                           value={password} onChange={onChangePassword}
                           errorMessage={passwordError} />
                <div className='flex justify-between'>
                    <Checkbox color='dark' value={hidePassword} label='Hide password' onChange={e => setHidePassword(e.target.checked)}/>
                    <a className='link-blue' href='/reset-password'>Forgot password?</a>
                </div>
            </div>
            <div>
                <button className='btn-dark w-full text-xl' onClick={onLogin}>Log in</button>
            </div>
        </div>
    </>
}

const LoginPageHeader = () => {
    return <Header>
        <a className='flex h-full items-center font-logo text-3xl' href='/'>
            <Logo/>
        </a>
        <div className='h-full flex justify-end items-center'>
            <a className='btn-dark-outline' href='/signup'>Create ID</a>
        </div>
    </Header>
}

const LoginPage = () => {
    return <>
        <div className='flex flex-col h-full'>
            <LoginPageHeader/>
            <main className='flex flex-col justify-center items-center grow'>
                <LoginForm/>
            </main>
        </div>
    </>
}

export default LoginPage