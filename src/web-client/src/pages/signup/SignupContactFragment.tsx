import {useLoading} from "../../hooks/use-loading";
import {useNavigate, useOutletContext} from "react-router";
import {SignupOutletContextType} from "./SignupPage";
import {ChangeEvent, useState} from "react";
import {validateEmail} from "./SignupValidation";
import InputText from "../../component/InputText";
import Button from "../../component/Button";

const SignupContactFragment = () => {

    const loading = useLoading()
    const navigate = useNavigate()
    const context = useOutletContext<SignupOutletContextType>()

    const [emailError, setEmailError] = useState<string | undefined>(undefined)

    function onChangeEmail(e: ChangeEvent<HTMLInputElement>) {
        let newValue = e.currentTarget.value
        if (newValue) {
            newValue = newValue.trim()
        }

        context.setEmail(newValue)
        setEmailError(undefined)
    }

    const onNext = () => {
        loading.show()

        const isEmailEmpty = context.email === undefined || context.email.trim().length === 0

        if (isEmailEmpty) {
            context.setEmail(undefined)
        } else {
            const email = context.email
            const validationResult = validateEmail(email!)
            if (validationResult.error) {
                setEmailError(validationResult.error)
                loading.hide()
                return
            }
        }

        navigate(context.getNextUrl())
        loading.hide()
    }

    return <>
        <main className='space-y-8'>
            <div className='text-center space-y-4'>
                <div>
                    <span className='text-2xl font-semibold'>Sign up</span>
                </div>
                <div>
                    <span className='text-base text-gray-400'>enter your contact data</span>
                </div>
            </div>
            <InputText label='Email' placeholder='mail@example.com' errorMessage={emailError}
                value={context.email} onChange={onChangeEmail}
                autoFocus={true}>
                Email is required for account recovery.
            </InputText>

            <Button type='dark' onClick={onNext}>Next</Button>
        </main>
    </>
}

export default SignupContactFragment