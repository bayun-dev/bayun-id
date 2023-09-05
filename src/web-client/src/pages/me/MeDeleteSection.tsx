import Account from "../../api/objects/Account";
import Button from "../../component/Button";
import * as React from "react";
import {ChangeEvent, useState} from "react";
import InputText from "../../component/InputText";
import * as PasswordValidator from "../../api/validation/PasswordValidator";
import Checkbox from "../../component/Checkbox";
import MethodMeDelete from "../../api/methods/MethodMeDelete";
import SwapSection, {SwapElementProps} from "../../component/SwapSection";
import ErrorBody, {ErrorType} from "../../api/objects/ErrorBody";
import {AxiosError} from "axios";
import useErrorPage from "../../hooks/useErrorPage";
import {useLoaderData} from "react-router";

const MeDeleteSwapElement = (props: SwapElementProps) => {

    const errorPage = useErrorPage()

    const [password, setPassword] = useState<string>('')
    const [passwordError, setPasswordError] = useState<string | undefined>(undefined)
    const [hidePassword, setHidePassword] = useState<boolean>(false)
    function onChangePassword(e: ChangeEvent<HTMLInputElement>) {
        setPassword(e.currentTarget.value)
        setPasswordError(undefined)
    }

    const [disabledDeleteButton, setDisabledDeleteButton] = useState<boolean>(false)

    async function onDelete() {
        const validationResult = PasswordValidator.quicklyValidate(password);
        setPasswordError(validationResult)
        if (validationResult) {
            return
        }

        setDisabledDeleteButton(true)

        const method = new MethodMeDelete({password})
        method.execute().then(r => {
            if (r.data.ok) {
                window.location.replace('/login')
            } else {
                throw ErrorType.INTERNAL_ERROR
            }
        }).catch((e: AxiosError<ErrorBody>) => {
            setDisabledDeleteButton(false)

            if (!(e.response && e.response.data)) {
                errorPage.show.internalError()
                return
            }

            const errorBody = e.response.data
            if (errorBody.type === ErrorType.BAD_REQUEST_PARAMETERS) {
                errorBody.parameters?.forEach(parameter => {
                    if (parameter === 'email') {
                        setPasswordError('Invalid')
                    } else {
                        // ignore
                    }
                })
            } if (errorBody.type === ErrorType.PASSWORD_INVALID) {
                setPasswordError('Incorrect')
            } else {
                errorPage.show.internalError()
            }
        })
    }

    return <>
        <div className='space-y-4 mt-4'>
            <InputText placeholder='Your password' label="Confirm it's you" value={password} onChange={onChangePassword} errorMessage={passwordError} autoFocus={true}/>
            <Checkbox color='dark' value={hidePassword} onChange={e => setHidePassword(e.target.checked)} label='Hide password'/>

            <div className='grid grid-cols-2 gap-4'>
                <Button type='light' onClick={props.swap}>Cancel</Button>
                <Button type='dark' onClick={onDelete} disabled={disabledDeleteButton}>Delete</Button>
            </div>
        </div>
    </>
}

const MeDeleteSection = () => {

    const me = useLoaderData() as Account
    const [swap, setSwap] = useState<boolean>(false)

    return <SwapSection title='Delete Bayun ID' description='You can delete your Bayun ID and related data'
                        swapElement={<MeDeleteSwapElement swap={() => setSwap(false)}/>} swap={swap} onClick={() => {setSwap(true)}}/>
}

export default MeDeleteSection