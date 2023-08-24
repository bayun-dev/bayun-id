import {useLoading} from "../../hooks/use-loading";
import PostLoginAvailabilityRequest from "../../api/requests/PostLoginAvailabilityRequest";
import IError, {IErrorCode} from "../../api/schemas/IError";
import {useNavigate, useOutletContext} from "react-router";
import {LoginOutletContextType} from "./LoginPage";
import InputText from "../../component/InputText";
import {ChangeEvent, useEffect, useState} from "react";
import Button from "../../component/Button";
import {validateUsername} from "./LoginValidation";
import {useLoginError} from "./LoginError";


const LoginStartFragment = () => {

    const loginError = useLoginError()
    const loading = useLoading()

    const navigate = useNavigate()
    const context = useOutletContext<LoginOutletContextType>()

    const [usernameError, setUsernameError] = useState<string | undefined>(undefined)
    const onChangeUsername = (e: ChangeEvent<HTMLInputElement>) => {
        let newValue = e.currentTarget.value
        if (newValue) {
            newValue = newValue.trim()
        }

        context.setUsername(newValue)
        setUsernameError(undefined)
    }


    const onNext = async () => {
        loading.show();

        const validationResult = validateUsername(context.username.trim());
        if (validationResult) {
            setUsernameError(validationResult)
            loading.hide()
            return
        }

        const request = new PostLoginAvailabilityRequest({
            username: context.username.trim()
        })
        request.execute().then(r => {
            if (r.status === 200) {
                if (r.data.available) {
                    navigate('/login/continue')
                } else {
                    if (r.data.reason === IErrorCode.USERNAME_NOT_OCCUPIED) {
                        setUsernameError('Account doesn\'t exist')
                    } else if (r.data.reason === IErrorCode.ACCOUNT_BLOCKED) {
                        setUsernameError('Account blocked')
                    } else if (r.data.reason === IErrorCode.ACCOUNT_DELETED) {
                        setUsernameError('Account deleted')
                    } else {
                        setUsernameError("Username not available")
                    }
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
                            setUsernameError('Invalid username')
                            break
                    }
                }
                loading.hide()
            } else {
                throw new Error()
            }
        }).catch(reason => {
            loading.hide()
            loginError.oops()
        })

    }

    // useEffect(() => {
    //     loginError.deleted()
    // }, [])

    return <>
        <main className='space-y-8'>
            <div className='text-center space-y-4'>
                <div>
                    <span className='text-2xl font-semibold'>Log in</span>
                </div>
                <div>
                    <span className='text-base text-gray-400'>with your Bayun ID</span>
                </div>
            </div>
            <InputText placeholder='Username' errorMessage={usernameError}
                       value={context.username} onChange={onChangeUsername}
                       autoFocus={true} />
            <Button type='dark' onClick={onNext}>Next</Button>
        </main>
    </>
}

export default LoginStartFragment