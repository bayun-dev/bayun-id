import Account from "../../api/objects/Account";
import Button from "../../component/Button";
import * as React from "react";
import {ChangeEvent, useState} from "react";
import InputText from "../../component/InputText";
import * as PasswordValidator from "../../api/validation/PasswordValidator";
import MethodMeSave from "../../api/methods/MethodMeSave";
import Checkbox from "../../component/Checkbox";
import SwapSection, {SwapElementProps} from "../../component/SwapSection";
import ErrorBody, {ErrorType} from "../../api/objects/ErrorBody";
import {AxiosError} from "axios";
import useErrorPage from "../../hooks/useErrorPage";
import {useLoaderData} from "react-router";

const MeLoginMethodsSwapElement = (props: SwapElementProps) => {

    const errorPage = useErrorPage()

    const [password, setPassword] = useState<string>('')
    const [passwordError, setPasswordError] = useState<string | undefined>(undefined)
    const [hidePassword, setHidePassword] = useState<boolean>(false)
    function onChangePassword(e: ChangeEvent<HTMLInputElement>) {
        const value = e.currentTarget.value
        setPassword(value)
        setPasswordError(PasswordValidator.inlineValidate(value))
    }

    const [disabledSaveButton, setDisabledSaveButton] = useState<boolean>(false)

    async function onSave() {
        const validationResult = PasswordValidator.fullValidate(password);
        setPasswordError(validationResult)
        if (validationResult) {
            return
        }

        setDisabledSaveButton(true)

        const method = new MethodMeSave({password})
        method.execute().then(r => {
            if (r.data.ok) {
                window.location.reload()
            } else {
                throw ErrorType.INTERNAL_ERROR
            }
        }).catch((e: AxiosError<ErrorBody>) => {
            setDisabledSaveButton(false)

            if (!(e.response && e.response.data)) {
                errorPage.show.internalError()
                return
            }

            const errorBody = e.response.data
            if (errorBody.type === ErrorType.BAD_REQUEST_PARAMETERS) {
                errorBody.parameters?.forEach(parameter => {
                    if (parameter === 'password') {
                        setPasswordError('Invalid')
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
            <InputText type={hidePassword ? 'password' : 'text'} placeholder='Your password' label='Password' value={password} onChange={onChangePassword} errorMessage={passwordError} autoFocus={true}/>
            <Checkbox label='Hide password' color='dark' value={hidePassword} onChange={e => setHidePassword(e.target.checked)}/>
            <div className='grid grid-cols-2 gap-4'>
                <Button type='light' onClick={props.swap}>Cancel</Button>
                <Button type='dark' onClick={onSave} disabled={disabledSaveButton}>Save</Button>
            </div>
        </div>
    </>
}

const MeLoginMethodsSection = () => {

    const me = useLoaderData() as Account
    const [swap, setSwap] = useState<boolean>(false)

    return <SwapSection title='Login methods' description='Make sure you can always access your Bayun ID by keeping this information up to date'
                        swapElement={<MeLoginMethodsSwapElement swap={() => setSwap(false)}/>} swap={swap} onClick={() => {setSwap(true)}}>
        <div className='flex gap-4'>
            <div className='w-16'>
                <span className='font-medium'>Password</span>
            </div>
            <div className='grow'>
                <span className='text-gray-500'>&bull;&bull;&bull;&bull;&bull;&bull;&bull;&bull;</span>
            </div>
        </div>
    </SwapSection>
}

export default MeLoginMethodsSection