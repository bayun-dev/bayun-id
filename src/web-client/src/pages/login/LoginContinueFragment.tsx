import {useLoading} from "../../hooks/use-loading";
import PostLoginRequest from "../../api/requests/PostLoginRequest";
import {useNavigate, useOutletContext} from "react-router";
import {LoginOutletContextType} from "./LoginPage";
import {ChangeEvent, useState} from "react";
import IError, {IErrorCode} from "../../api/schemas/IError";
import InputText from "../../component/InputText";
import Button from "../../component/Button";
import Checkbox from "../../component/Checkbox";
import Link from "../../component/Link";
import {useLoginError} from "./LoginError";
import {validatePassword} from "./LoginValidation";

const LoginContinueFragment = () => {
    
    const loading = useLoading()
    const navigate = useNavigate()
    const loginError = useLoginError()
    const context = useOutletContext<LoginOutletContextType>()

    const [password, setPassword] = useState<string>('')
    const [passwordError, setPasswordError] = useState<string | undefined>()
    const [showPassword, setShowPassword] = useState<boolean>(false)

    const onChangePassword = (e: ChangeEvent<HTMLInputElement>) => {
        let newValue = e.currentTarget.value
        if (newValue) {
            newValue = newValue.trim()
        }

        setPassword(newValue)
        setPasswordError(undefined)
    }

    const onLogin = async () => {
        loading.show()

        const validationResult = validatePassword(password)
        if (validationResult) {
            setPasswordError(validationResult)
            loading.hide()
            return
        }

        const request = new PostLoginRequest({
            username: context.username,
            password: password
        })
        request.execute().then(r => {
            if (r.status === 200) {
                window.location.replace(r.data.redirectUrl)
                loading.hide()
            } else {
                throw new Error()
            }
        }, (t) => {
            if (t.response.data.errors) {
                const errors: IError[] = t?.response?.data?.errors
                for (let error of errors) {
                    switch (error.code) {
                        case IErrorCode.ACCOUNT_BLOCKED:
                            throw new Error('blocked')
                        case IErrorCode.ACCOUNT_DELETED:
                            throw new Error('deleted')
                        case IErrorCode.USERNAME_INVALID:
                            throw new Error()
                        case IErrorCode.PASSWORD_INVALID:
                            setPasswordError('invalid password')
                            break
                        case IErrorCode.CREDENTIALS_NOT_FOUND:
                            setPasswordError('incorrect password')
                            break
                    }
                }
                loading.hide()
            } else {
                throw new Error()
            }
        }).catch((error: Error) => {
            loading.hide()
            switch (error.message) {
                case 'blocked':
                    loginError.blocked()
                    break
                case 'deleted':
                    loginError.deleted()
                    break
                default:
                    loginError.oops()
            }
        })
    }

    return <>
        <main className='space-y-8'>
            <div className='text-center space-y-4'>
                <div>
                    <span className='text-2xl font-semibold'>Log in</span>
                </div>
                <div>
                    <span className='text-base text-gray-400'>Welcome back</span>
                </div>
            </div>
            <div className='space-y-4'>
                <InputText placeholder='Username' defaultValue={context.username} disabled={true}/>
                <InputText placeholder='Password' autoFocus={true}
                           type={showPassword ? 'text' : 'password'}
                           value={password} onChange={onChangePassword}
                           errorMessage={passwordError} />
                <div className='flex justify-between'>
                    <Checkbox color='dark' value={showPassword} label='Show password' onChange={e => setShowPassword(e.target.checked)}/>
                    <Link href='/restore/password'>Forgot password?</Link>
                </div>
            </div>
            <div>
                <Button type='dark' onClick={onLogin}>Log in</Button>
            </div>
        </main>
    </>
}

export default LoginContinueFragment