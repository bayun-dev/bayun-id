import Account from "../../api/objects/Account";
import Button from "../../component/Button";
import * as React from "react";
import {ChangeEvent, useState} from "react";
import InputText from "../../component/InputText";
import * as EmailValidator from "../../api/validation/EmailValidator";
import MethodMeSave from "../../api/methods/MethodMeSave";
import SwapSection, {SwapElementProps} from "../../component/SwapSection";
import ErrorBody, {ErrorType} from "../../api/objects/ErrorBody";
import {AxiosError} from "axios";
import useErrorPage from "../../hooks/useErrorPage";
import {useLoaderData} from "react-router";


const MeContactSwapElement = (props: SwapElementProps) => {

    const errorPage = useErrorPage()

    const [email, setEmail] = useState<string>('')
    const [emailError, setEmailError] = useState<string | undefined>(undefined)
    function onChangeEmail(e: ChangeEvent<HTMLInputElement>) {
        setEmail(e.currentTarget.value.trim())
        setEmailError(undefined)
    }

    const [disabledSaveButton, setDisabledSaveButton] = useState<boolean>(false)

    async function onSave() {
        const validationResult = EmailValidator.quicklyValidate(email);
        setEmailError(validationResult)
        if (validationResult) {
            return
        }

        setDisabledSaveButton(true)

        const method = new MethodMeSave({email})
        method.execute().then(r => {
            if (r.data.ok) {
                window.location.reload()
            } else {
                throw ErrorType.INTERNAL
            }
        }).catch((e: AxiosError<ErrorBody>) => {
            setDisabledSaveButton(false)

            if (!(e.response && e.response.data)) {
                errorPage.show.internalError()
                return
            }

            const errorBody = e.response.data
            if (errorBody.type === ErrorType.INVALID_REQUEST_PARAM) {
                errorBody.parameters?.forEach(parameter => {
                    if (parameter === 'email') {
                        setEmailError('Invalid')
                    } else {
                        // ignore
                    }
                })
            } else {
                errorPage.show.internalError()
            }
        })
    }

    return <>
        <div className='space-y-4 mt-4'>
            <InputText placeholder='Your email' label='Email' value={email} onChange={onChangeEmail} errorMessage={emailError} autoFocus={true}/>
            <div className='grid grid-cols-2 gap-4'>
                <Button type='light' onClick={props.swap}>Cancel</Button>
                <Button type='dark' onClick={onSave} disabled={disabledSaveButton}>Save</Button>
            </div>
        </div>
    </>
}

const MeContactSection = () => {

    const me = useLoaderData() as Account

    const [swap, setSwap] = useState<boolean>(false)

    return <SwapSection title='Contact' description='Email is necessary to be able to restore access to your account and receive security alerts'
                        swap={swap} onClick={() => setSwap(true)}
                        swapElement={<MeContactSwapElement swap={() => setSwap(false)}/>}>
        <div className='flex gap-4'>
            <div className='w-16'>
                <span className='font-medium'>Email</span>
            </div>
            <div className='grow'>
                    <span className='text-gray-500'>
                    {me.email === null ? <>not specified</> : <>{me.email}</> }
                    </span>

            </div>
        </div>
        {me.emailConfirmed !== null && !me.emailConfirmed && <>
            <div className='p-4 bg-orange-50 border border-orange-100 rounded-lg mt-4'>
                <div>
                    <span className='font-medium'>Email awaiting confirmation</span>
                </div>
                <div>
                    <span className='text-gray-500 text-sm'>An email with a confirmation link was sent to <span className='font-medium'>{me.email}</span>. Please check your inbox.</span>
                </div>
            </div>
        </>}
    </SwapSection>
}

export default MeContactSection