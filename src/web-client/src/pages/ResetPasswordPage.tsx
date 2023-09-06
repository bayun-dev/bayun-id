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
import MethodAuthResetPassword from "../api/methods/MethodAuthResetPassword";

const ResetPasswordForm = () => {

    const errorPage = useErrorPage()

    const [successEmail, setSuccessEmail] = useState<string | undefined>(undefined)
    const [isEmailNotConfirmed, setIsEmailNotConfirmed] = useState<boolean>(false)

    const [username, setUsername] = useState<string>('')
    const [usernameError, setUsernameError] = useState<string | undefined>(undefined)
    const onChangeUsername = (e: ChangeEvent<HTMLInputElement>) => {
        const value = e.currentTarget.value.trim()
        setUsername(value)
        setUsernameError(undefined)
    }

    async function onResetPassword() {
        const usernameValidationResult = UsernameValidator.quicklyValidate(username);
        if (usernameValidationResult) {
            setUsernameError(usernameValidationResult)
            return
        }

        const method = new MethodAuthResetPassword({username})
        method.execute().then(r => {
            if (r.data.ok) {
                setSuccessEmail(r.data.email)
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
                    } else {
                        // ignore
                    }
                })
            } else if (errorBody.type === ErrorType.EMAIL_NOT_CONFIRMED) {
                setIsEmailNotConfirmed(true)
            } else if (errorBody.type === ErrorType.USERNAME_UNOCCUPIED) {
                setUsernameError('Account doesn\'t exist')
            } else {
                errorPage.show.internalError()
            }
        })
    }

    return <>
        <div className='w-full xs:w-xs p-4 space-y-8'>
            <div className='text-center'>
                <span className='text-3xl font-medium'>Account recovery</span>
            </div>
            { isEmailNotConfirmed ? <>
                <div className='text-center'>
                    <span className='text-red-500'>Sorry, we can't recover your account because you didn't confirm your email.</span>
                </div>
            </> : <>
                { successEmail ? <>
                    <div>
                        <span>New password sent to <span className='font-semibold'>{successEmail}</span></span>
                    </div>
                </> : <>
                    <InputText placeholder='Username' autoFocus={true}
                               value={username} onChange={onChangeUsername}
                               errorMessage={usernameError} />
                    <button className='btn-dark w-full text-xl' onClick={onResetPassword}>Reset password</button>
                </>}
            </> }
        </div>
    </>
}

const RestPasswordPageHeader = () => {
    return <Header>
        <a className='flex h-full items-center font-logo text-3xl' href='/'>
            <Logo/>
        </a>
        <div className='h-full flex justify-end items-center'>
            <a className='btn-dark-outline' href='/login'>Log In</a>
        </div>
    </Header>
}

const ResetPasswordPage = () => {
    return <>
        <div className='flex flex-col h-full'>
            <RestPasswordPageHeader/>
            <main className='flex flex-col justify-center items-center grow'>
                <ResetPasswordForm/>
            </main>
        </div>
    </>
}

export default ResetPasswordPage