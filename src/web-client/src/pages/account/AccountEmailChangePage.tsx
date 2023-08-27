import InputText from "../../component/InputText";
import Select from "../../component/Select";
import InputDate from "../../component/InputDate";
import Button from "../../component/Button";
import {ChangeEvent, ReactNode, useEffect, useState} from "react";
import moment from "moment";
import {useNavigate, useOutletContext} from "react-router";
import {AccountPageContextType} from "./AccountPage";
import {
    validateDateOfBirth,
    validateEmail,
    validateFirstname,
    validateGender,
    validateLastname
} from "../signup/SignupValidation";
import {useLoading} from "../../hooks/use-loading";
import PatchAccountsByIdRequest from "../../api/requests/PatchAccountsByIdRequest";
import {AxiosError} from "axios";
import IError, {IErrorCode} from "../../api/schemas/IError";
import * as React from "react";
import {SvgEnvelope} from "../../component/svg";

const AccountEmailChangePage = () => {

    const context = useOutletContext<AccountPageContextType>()
    const navigate = useNavigate()

    const [submitDisabled, setSubmitDisabled] = useState<boolean>(true)

    const [email, setEmail] = useState<string>('')
    const [emailError, setEmailError] = useState<string | undefined>(undefined)
    function onChangeEmail(e: ChangeEvent<HTMLInputElement>) {
        let newValue = e.currentTarget.value
        if (newValue) {
            newValue = newValue.trim()
        }

        setEmail(newValue)
        setEmailError(undefined)
    }

    const [nameHelp, setNameHelp] = useState<string | ReactNode | undefined>(undefined)

    useEffect(() => {
        setSubmitDisabled(email.trim().length === 0 || email.trim() === context.account.contact?.email)
    }, [email])

    async function onSave() {
        const validateEmailResult = validateEmail(email)
        if (validateEmailResult.error) {
            setEmailError(validateEmailResult.error)
            if (validateEmailResult.help) {
                setNameHelp(validateEmailResult.help)
            }
        }

        if (validateEmailResult.error) {
            return
        }

        const request = new PatchAccountsByIdRequest('me', {
            email: email
        })
        request.execute().then(r => {
            if (r.status === 200) {
                navigate('/security')
            } else {
                throw r
            }
        }, (t) => {
            if (t.response.data.errors) {
                const errors: IError[] = t?.response?.data?.errors
                errors.forEach(error => {
                    if (error.code === IErrorCode.EMAIL_INVALID) {
                        setEmailError('invalid email')
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
                <SvgEnvelope className='w-12 h-12 place-self-center'/>
            </div>
            <div className='text-center'>
                <span className='font-medium text-xl'>Enter your email</span>
            </div>
            <div className='text-center w-3/4'>
                <span className='text-sm text-gray-500'>
                    Enter your email address to be able to restore access to your account and receive security notifications
                </span>
            </div>
            <div className='w-2/3'>
                <InputText autoFocus={true} value={email} onChange={onChangeEmail} errorMessage={emailError}/>
            </div>
            <div className='w-1/3'>
                <Button type='dark' onClick={onSave} disabled={submitDisabled}>Save</Button>
            </div>
        </div>
    </>
}

export default AccountEmailChangePage