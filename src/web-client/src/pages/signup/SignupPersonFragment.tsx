import {useLoading} from "../../hooks/use-loading";
import {useNavigate, useOutletContext} from "react-router";
import {SignupOutletContextType} from "./SignupPage";
import {ChangeEvent, ReactNode, useEffect, useState} from "react";
import {validateDateOfBirth, validateFirstname, validateGender, validateLastname} from "./SignupValidation";
import InputText from "../../component/InputText";
import Button from "../../component/Button";
import InputDate from "../../component/InputDate";
import Select from "../../component/Select";

const SignupPersonFragment = () => {

    const loading = useLoading()
    const navigate = useNavigate()
    const context = useOutletContext<SignupOutletContextType>()

    const [firstNameError, setFirstNameError] = useState<string | undefined>(undefined)
    const [lastNameError, setLastNameError] = useState<string | undefined>(undefined)
    const [dateOfBirthError, setDateOfBirthError] = useState<string | undefined>(undefined)
    const [genderError, setGenderError] = useState<string | undefined>(undefined)
    const [nameHelp, setNameHelp] = useState<string | ReactNode | undefined>(undefined)

    const onChangeFirstName = (e: ChangeEvent<HTMLInputElement>) => {
        let newValue = e.currentTarget.value
        if (newValue) {
            newValue = newValue.trim()
        }

        context.setFirstName(newValue)
        setFirstNameError(undefined)
    }

    const onChangeLastName = (e: ChangeEvent<HTMLInputElement>) => {
        let newValue = e.currentTarget.value
        if (newValue) {
            newValue = newValue.trim()
        }

        context.setLastName(newValue)
        setLastNameError(undefined)
    }

    const onChangeDateOfBirth = (date: Date | null) => {
        if (date != null) {
            context.setDateOfBirth(date)
        }
    }

    function onChangeGender(e: ChangeEvent<HTMLSelectElement>) {
        let newValue = e.target.value
        if (newValue) {
            newValue = newValue.trim()
        }

        context.setGender(newValue)
        setGenderError(undefined)
    }

    const onNext = () => {
        loading.show()

        const firstName = context.firstName.trim()
        const validateFirstnameResult = validateFirstname(firstName)
        if (validateFirstnameResult.error) {
            setFirstNameError(validateFirstnameResult.error)
            if (validateFirstnameResult.help) {
                setNameHelp(validateFirstnameResult.help)
            }

        }

        const lastName = context.lastName.trim()
        const validateLastnameResult = validateLastname(lastName)
        if (validateLastnameResult.error) {
            setLastNameError(validateLastnameResult.error)
            if (validateLastnameResult.help) {
                setNameHelp(validateLastnameResult.help)
            }
        }

        const dateOfBirth = context.dateOfBirth
        const validateDateOfBirthResult = validateDateOfBirth(dateOfBirth)
        if (validateDateOfBirthResult.error) {
            setDateOfBirthError(validateDateOfBirthResult.error)
        }

        const gender = context.gender.trim()
        const validateGenderResult = validateGender(gender)
        if (validateGenderResult.error) {
            setGenderError(validateGenderResult.error)
        }

        if (validateFirstnameResult.error || validateLastnameResult.error
            || validateDateOfBirthResult.error || validateGenderResult.error) {
            loading.hide()
            return
        }

        context.setFirstName(firstName)
        context.setLastName(lastName)
        context.setDateOfBirth(dateOfBirth)
        context.setGender(gender)

        navigate(context.getNextUrl())
        loading.hide()
    }

    useEffect(() => {
        console.log('gender', context.gender)
    }, [context.gender])

    return <>
        <main className='space-y-8'>
            <div className='text-center space-y-4'>
                <div>
                    <span className='text-2xl font-semibold'>Sign up</span>
                </div>
                <div>
                    <span className='text-base text-gray-400'>enter your personal data</span>
                </div>
            </div>
            <InputText placeholder='First name' errorMessage={firstNameError}
                       value={context.firstName} onChange={onChangeFirstName}
                       autoFocus={true}/>
            <InputText placeholder='Last name' errorMessage={lastNameError}
                       value={context.lastName} onChange={onChangeLastName}>{ nameHelp }</InputText>
            <div className='grid grid-cols-2 gap-4'>
                <Select label='Gender' values={{male: 'Male', female: 'Female'}}
                        value={context.gender} onChange={onChangeGender} errorMessage={genderError}/>
                <InputDate label='Date of birth' placeholder='dd.mm.yyyy' errorMessage={dateOfBirthError}
                           selected={context.dateOfBirth} onChange={date => onChangeDateOfBirth(date)}/>
            </div>

            <Button type='dark' onClick={onNext}>Next</Button>
        </main>
    </>
}

export default SignupPersonFragment