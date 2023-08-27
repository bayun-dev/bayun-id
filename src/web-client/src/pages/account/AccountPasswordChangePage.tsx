import InputText from "../../component/InputText";
import Button from "../../component/Button";
import * as React from "react";
import {ChangeEvent, ReactNode, useEffect, useState} from "react";
import {useNavigate, useOutletContext} from "react-router";
import {AccountPageContextType} from "./AccountPage";
import {validatePassword} from "../signup/SignupValidation";
import PatchAccountsByIdRequest from "../../api/requests/PatchAccountsByIdRequest";
import IError, {IErrorCode} from "../../api/schemas/IError";
import {SvgKey} from "../../component/svg";
import Checkbox from "../../component/Checkbox";

const AccountPasswordChangePage = () => {

    const context = useOutletContext<AccountPageContextType>()
    const navigate = useNavigate()

    const [submitDisabled, setSubmitDisabled] = useState<boolean>(true)
    const [showPassword, setShowPassword] = useState<boolean>(false)

    const [password, setPassword] = useState<string>('')
    const [passwordError, setPasswordError] = useState<string | undefined>(undefined)
    function onChangePassword(e: ChangeEvent<HTMLInputElement>) {
        let newValue = e.currentTarget.value
        if (newValue) {
            newValue = newValue.trim()
        }

        setPassword(newValue)
        setPasswordError(undefined)
    }

    useEffect(() => {
        setSubmitDisabled(password.trim().length < 8)
    }, [password])

    async function onSave() {
        const validatePasswordResult = validatePassword(password)
        if (validatePasswordResult.error) {
            setPasswordError(validatePasswordResult.error)
        }

        if (validatePasswordResult.error) {
            return
        }

        const request = new PatchAccountsByIdRequest('me', {
            password: password
        })
        request.execute().then(r => {
            if (r.status === 200) {
                navigate('/security', { replace: true, relative: 'route' })
            } else {
                throw r
            }
        }, (t) => {
            if (t.response.data.errors) {
                const errors: IError[] = t?.response?.data?.errors
                errors.forEach(error => {
                    if (error.code === IErrorCode.EMAIL_INVALID) {
                        setPasswordError('invalid password')
                    } else {
                        throw error
                    }
                })
            } else {
                throw new Error()
            }
        }).catch(error => {
            console.error(error)
            setSubmitDisabled(false)
        })
    }

    return <>
        <div className='flex flex-col items-center p-4 gap-4 border border-gray-200 shadow-sm rounded-lg'>
            <div className='grid'>
                <SvgKey className='w-12 h-12 place-self-center'/>
            </div>
            <div className='text-center'>
                <span className='font-medium text-xl'>Create a password</span>
            </div>
            <div className='text-center w-3/4'>
                <span className='text-sm text-gray-500'>Password will protect your account from hacking</span>
            </div>

            <div className='w-3/4'>
                <InputText type={showPassword ? 'text' : 'password'} autoFocus={true} value={password} onChange={onChangePassword} errorMessage={passwordError}>
                    The password can contain any characters and must be between 8 and 60 characters long.
                </InputText>
                <div className='mt-2'>
                    <Checkbox label='Show password' value={showPassword} onChange={e => setShowPassword(e.target.checked)}/>
                </div>
            </div>
            <div className='w-2/3'>

            </div>
            <div className='w-1/3'>
                <Button type='dark' onClick={onSave} disabled={submitDisabled}>Save</Button>
            </div>
        </div>
    </>
}

export default AccountPasswordChangePage