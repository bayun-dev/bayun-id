import {useLoading} from "../../hooks/use-loading";
import {useNavigate, useOutletContext} from "react-router";
import {SignupOutletContextType} from "./SignupPage";
import PostSignupAvailabilityRequest from "../../api/requests/PostSignupAvailabilityRequest";
import IError, {IErrorCode} from "../../api/schemas/IError";
import {ChangeEvent, ReactNode, useState} from "react";
import InputText from "../../component/InputText";
import Button from "../../component/Button";
import {validateUsername} from "./SignupValidation";
import {useSignupError} from "./SignupError";

const SignupUsernameFragment = () => {

    const loading = useLoading()
    const navigate = useNavigate()
    const context = useOutletContext<SignupOutletContextType>()
    const signupError = useSignupError()

    const [usernameError, setUsernameError] = useState<string | undefined>(undefined)
    const [usernameHelp, setUsernameHelp] = useState<string | ReactNode | undefined>(undefined)
    const onChangeUsername = (e: ChangeEvent<HTMLInputElement>) => {
        let newValue = e.currentTarget.value
        if (newValue) {
            newValue = newValue.trim()
        }

        context.setUsername(newValue)
        setUsernameError(undefined)
    }

    const onNext = async () => {
        loading.show()

        const username = context.username.trim();
        const validationResult = validateUsername(username);
        if (validationResult.error) {
            setUsernameError(validationResult.error)
            setUsernameHelp(validationResult.help)
            loading.hide()
            return
        }

        const request = new PostSignupAvailabilityRequest({
            username: username
        })
        request.execute().then(r => {
            if (r.status === 200) {
                if (r.data.available) {
                    navigate(context.getNextUrl())
                } else {
                    setUsernameError('This username is already taken')
                }
                loading.hide()
            } else {
                throw new Error()
            }
        }, t => {
            if (t.response.data.errors) {
                const errors: IError[] = t?.response?.data?.errors
                for (let error of errors) {
                    switch (error.code) {
                        case IErrorCode.USERNAME_INVALID:
                            setUsernameError('invalid username')
                            break
                        default:
                            throw new Error()
                    }
                }
                loading.hide()
            } else {
                throw new Error()
            }
        }).catch(error => {
            signupError.oops()
            loading.hide()
        })
    }

    return <>
        <main className='space-y-8'>
            <div className='text-center space-y-4'>
                <div>
                    <span className='text-2xl font-semibold'>Sign up</span>
                </div>
                <div>
                    <span className='text-base text-gray-400'>enter your new username</span>
                </div>
            </div>
            <InputText placeholder='Username' errorMessage={usernameError}
                       value={context.username} onChange={onChangeUsername}
                       autoFocus={true}>{ usernameHelp }</InputText>
            <Button type='dark' onClick={onNext}>Next</Button>
        </main>
    </>
}

export default SignupUsernameFragment