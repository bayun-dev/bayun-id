import {useLoading} from "../../hooks/use-loading";
import {useOutletContext} from "react-router";
import {SignupOutletContextType} from "./SignupPage";
import PostSignupRequest from "../../api/requests/PostSignupRequest";
import IError, {IErrorCode} from "../../api/schemas/IError";
import InputText from "../../component/InputText";
import Button from "../../component/Button";
import {ChangeEvent, ReactNode, useState} from "react";
import {validatePassword} from "./SignupValidation";
import moment from "moment";
import {useSignupError} from "./SignupError";
import Checkbox from "../../component/Checkbox";

const SignupSecretFragment = () => {

    const loading = useLoading()
    const context = useOutletContext<SignupOutletContextType>()
    const signupError = useSignupError()

    const [password, setPassword] = useState<string>('')
    const [passwordError, setPasswordError] = useState<string | undefined>(undefined)
    const [passwordHelp, setPasswordHelp] = useState<string | ReactNode | undefined>(undefined)
    const [showPassword, setShowPassword] = useState<boolean>(false)
    function onChangePassword(e: ChangeEvent<HTMLInputElement>) {
        setPassword(e.currentTarget.value)
        setPasswordError(undefined)
    }

    const onNext = () => {
        loading.show()

        const validationResult = validatePassword(password)
        if (validationResult.error) {
            setPasswordError(validationResult.error)
            if (validationResult.help) {
                setPasswordHelp(validationResult.help)
            }

            loading.hide()
            return
        }

        const request = new PostSignupRequest({
            username: context.username,
            firstName: context.firstName,
            lastName: context.lastName,
            dateOfBirth: moment(context.dateOfBirth).format('DD.MM.YYYY'),
            gender: context.gender,
            email: context.email,
            password: password,
        })
        request.execute().then(r => {
            if (r.status === 200) {
                window.location.replace(context.getNextUrl())
                loading.hide()
            } else {
                throw new Error()
            }
        }, (t) => {
            if (t.response.data.errors) {
                const errors: IError[] = t?.response?.data?.errors
                if (errors.some(value => value.code !== IErrorCode.PASSWORD_INVALID)) {
                    throw new Error()
                } else if (errors.some(value => value.code === IErrorCode.PASSWORD_INVALID)) {
                    setPasswordError("invalid password")
                }
                loading.hide()
            } else {
                throw new Error()
            }
        }).catch(error => {
            loading.hide()
            signupError.oops()
        })
    }

    return <>
        <main className='space-y-8'>
            <div className='text-center space-y-4'>
                <div>
                    <span className='text-2xl font-semibold'>Sign up</span>
                </div>
                <div>
                    <span className='text-base text-gray-400'>create a strong password</span>
                </div>
            </div>
            <div className='space-y-4'>
                <InputText label='Password' placeholder='Password' errorMessage={passwordError}
                           type={showPassword ? 'text' : 'password'}
                           value={password} onChange={onChangePassword}
                           autoFocus={true}>{passwordHelp}</InputText>
                <Checkbox color='dark' value={showPassword} label='Show password' onChange={e => setShowPassword(e.target.checked)}/>
            </div>
            <Button type='dark' onClick={onNext}>Next</Button>
        </main>
    </>
}

export default SignupSecretFragment