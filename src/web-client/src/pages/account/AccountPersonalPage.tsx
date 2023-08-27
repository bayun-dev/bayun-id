import InputText from "../../component/InputText";
import Select from "../../component/Select";
import InputDate from "../../component/InputDate";
import Button from "../../component/Button";
import {ChangeEvent, ReactNode, useEffect, useState} from "react";
import moment from "moment";
import {useNavigate, useOutletContext} from "react-router";
import {AccountPageContextType} from "./AccountPage";
import {validateDateOfBirth, validateFirstname, validateGender, validateLastname} from "../signup/SignupValidation";
import {useLoading} from "../../hooks/use-loading";
import PatchAccountsByIdRequest from "../../api/requests/PatchAccountsByIdRequest";
import {AxiosError} from "axios";
import IError, {IErrorCode} from "../../api/schemas/IError";
import * as React from "react";

const AccountPersonalPage = () => {

    const context = useOutletContext<AccountPageContextType>()
    const navigate = useNavigate()

    const [submitDisabled, setSubmitDisabled] = useState<boolean>(true)
    const [changed, setChanged] = useState<boolean>(false)

    const [firstName, setFirstName] = useState<string>(context.account.person?.firstName!)
    const [firstNameError, setFirstNameError] = useState<string | undefined>(undefined)
    function onChangeFirstName(e: ChangeEvent<HTMLInputElement>) {
        let newValue = e.currentTarget.value
        if (newValue) {
            newValue = newValue.trim()
        }

        setFirstName(newValue)
        setFirstNameError(undefined)
    }

    const [lastName, setLastName] = useState<string>(context.account.person?.lastName!)
    const [lastNameError, setLastNameError] = useState<string | undefined>(undefined)
    function onChangeLastName(e: ChangeEvent<HTMLInputElement>) {
        let newValue = e.currentTarget.value
        if (newValue) {
            newValue = newValue.trim()
        }

        setLastName(newValue)
        setLastNameError(undefined)
    }

    const [nameHelp, setNameHelp] = useState<string | ReactNode | undefined>(undefined)

    const [dateOfBirth, setDateOfBirth] = useState<Date>(moment(context.account.person?.dateOfBirth!, 'DD.MM.yyyy').toDate())
    const [dateOfBirthError, setDateOfBirthError] = useState<string | undefined>(undefined)
    const onChangeDateOfBirth = (date: Date | null) => {
        if (date != null) {
            setDateOfBirth(date)
        }
    }

    const [gender, setGender] = useState<string>(context.account.person?.gender!)
    const [genderError, setGenderError] = useState<string | undefined>(undefined)
    function onChangeGender(e: ChangeEvent<HTMLSelectElement>) {
        let newValue = e.target.value
        if (newValue) {
            newValue = newValue.trim()
        }

        setGender(newValue)
        setGenderError(undefined)
    }

    useEffect(() => {
        setSubmitDisabled(firstName === context.account.person?.firstName
            && lastName === context.account.person?.lastName
            && moment(dateOfBirth).format('DD.MM.yyyy') === context.account.person?.dateOfBirth
            && gender === context.account.person?.gender)
    }, [firstName, lastName, dateOfBirth, gender])

    function onChanged() {
        setChanged(true)
        setTimeout(() => {
            setChanged(false)
        }, 5000)
    }

    async function onSave() {
        setSubmitDisabled(true)

        const validateFirstnameResult = validateFirstname(firstName)
        if (validateFirstnameResult.error) {
            setFirstNameError(validateFirstnameResult.error)
            if (validateFirstnameResult.help) {
                setNameHelp(validateFirstnameResult.help)
            }
        }

        const validateLastnameResult = validateLastname(lastName)
        if (validateLastnameResult.error) {
            setLastNameError(validateLastnameResult.error)
            if (validateLastnameResult.help) {
                setNameHelp(validateLastnameResult.help)
            }
        }

        const validateDateOfBirthResult = validateDateOfBirth(dateOfBirth)
        if (validateDateOfBirthResult.error) {
            setDateOfBirthError(validateDateOfBirthResult.error)
        }

        const validateGenderResult = validateGender(gender)
        if (validateGenderResult.error) {
            setGenderError(validateGenderResult.error)
        }

        if (validateFirstnameResult.error || validateLastnameResult.error
            || validateDateOfBirthResult.error || validateGenderResult.error) {
            setSubmitDisabled(false)
            return
        }

        const dateOfBirthFormat = moment(dateOfBirth).format('DD.MM.yyyy')

        const request = new PatchAccountsByIdRequest('me', {
            firstName: context.account.person?.firstName === firstName ? undefined : firstName,
            lastName: context.account.person?.lastName === lastName ? undefined : lastName,
            dateOfBirth: context.account.person?.dateOfBirth === dateOfBirthFormat ? undefined : dateOfBirthFormat,
            gender: context.account.person?.gender.toLowerCase() === gender.toLowerCase() ? undefined : gender.toLowerCase(),
        })
        request.execute().then(r => {
            if (r.status === 200) {
                navigate(0)
            } else {
                throw r
            }
        }, (t) => {
            if (t.response.data.errors) {
                const errors: IError[] = t?.response?.data?.errors
                errors.forEach(error => {
                    if (error.code === IErrorCode.FIRSTNAME_INVALID) {
                        setFirstNameError('invalid first name')
                    } else if (error.code === IErrorCode.LASTNAME_INVALID) {
                        setLastNameError('invalid last name')
                    } else if (error.code === IErrorCode.DATE_OF_BIRTH_INVALID) {
                        setDateOfBirthError('invalid date of birth')
                    } else if (error.code === IErrorCode.GENDER_INVALID) {
                        setGenderError('invalid gender')
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
        { changed &&
            <div className="p-4 mb-4 text-base text-green-800 rounded-lg bg-green-50 dark:bg-gray-800 dark:text-green-400 border border-green-300 dark:border-green-300">
                <span className="">Changes saved</span>
            </div>
        }
        <div className='grid grid-cols-2 p-4 gap-x-4 border border-gray-200 shadow-sm rounded-lg'>
            <InputText label='First Name' value={firstName} defaultValue={context.account.person?.firstName!}
                       onChange={onChangeFirstName} errorMessage={firstNameError}/>
            <InputText label='Last Name' value={lastName} defaultValue={context.account.person?.lastName!}
                       onChange={onChangeLastName} errorMessage={lastNameError}/>
            <div className='col-span-2 mb-8'>
                { nameHelp &&
                    <div id="helper-text-explanation" className="mt-2 text-sm text-gray-500 dark:text-gray-400">{nameHelp}</div>
                }
            </div>
            <Select label='Gender' value={gender} values={{male: 'Male', female: 'Female'}}
                       onChange={onChangeGender} errorMessage={genderError}/>
            <InputDate label='Date of birth' dateFormat='dd.MM.yyyy'
                       selected={dateOfBirth} onChange={onChangeDateOfBirth} errorMessage={dateOfBirthError}/>
            <div className='col-span-2 mt-8'>
                <Button type='dark' onClick={onSave} disabled={submitDisabled}>Save</Button>
            </div>
        </div>
    </>
}

export default AccountPersonalPage