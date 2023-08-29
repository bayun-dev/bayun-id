import InputText from "../../component/InputText";
import Select from "../../component/Select";
import InputDate from "../../component/InputDate";
import Button from "../../component/Button";
import * as React from "react";
import {ChangeEvent, Dispatch, ReactNode, SetStateAction, useEffect, useRef, useState} from "react";
import moment from "moment";
import {useNavigate, useOutletContext} from "react-router";
import {AccountPageContextType} from "./AccountPage";
import {validateDateOfBirth, validateFirstname, validateGender, validateLastname} from "../signup/SignupValidation";
import PatchAccountsByIdRequest from "../../api/requests/PatchAccountsByIdRequest";
import IError, {IErrorCode} from "../../api/schemas/IError";
import DeleteAccountsByIdAvatarRequest from "../../api/requests/DeleteAccountsByIdAvatarRequest";

const AccountPersonalPage = () => {

    const context = useOutletContext<AccountPageContextType>()
    const navigate = useNavigate()

    const [submitDisabled, setSubmitDisabled] = useState<boolean>(true)
    const [changed, setChanged] = useState<boolean>(false)

    const [avatarSrc, setAvatarSrc] = useState<string>(`/avatar/${context.account.avatarId}/large`)
    const [avatarFile, setAvatarFile] = useState<File | undefined>(undefined)

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
        console.log('update submit', avatarSrc === `/avatar/${context.account.avatarId}/large`)
        setSubmitDisabled(firstName === context.account.person?.firstName
            && lastName === context.account.person?.lastName
            && moment(dateOfBirth).format('DD.MM.yyyy') === context.account.person?.dateOfBirth
            && gender === context.account.person?.gender
            && avatarSrc === `/avatar/${context.account.avatarId}/large`)
    }, [firstName, lastName, dateOfBirth, gender, avatarSrc])

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
            avatar: avatarFile
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

    function onDeleteAvatar() {
        const request = new DeleteAccountsByIdAvatarRequest('me');
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
            <div className='col-span-2 flex gap-4 mb-4 '>
                <Avatar src={avatarSrc} setSrc={setAvatarSrc} setFile={setAvatarFile} onDelete={onDeleteAvatar}/>
                <div className='flex flex-col justify-center'>
                    <div>
                        <span className='font-medium text-lg'>{context.account.person?.firstName} {context.account.person?.lastName}</span>
                    </div>
                    <div>
                        <span className='text-sm text-gray-500'>ID: {context.account.id}</span>
                    </div>
                </div>
            </div>
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

const Avatar = (props: { src: string, setSrc: Dispatch<SetStateAction<string>>, setFile: Dispatch<SetStateAction<File | undefined>>, onDelete: () => void }) => {

    const [open, setOpen] = useState<boolean>(false)

    const wrapperRef = useRef<HTMLDivElement | null>(null);

    const dropVisibleClasses = open ? '' : 'hidden '
    const dropCommonClasses = 'absolute bg-white border border-gray-200 rounded-lg shadow w-44 dark:bg-gray-700 '
    const dropPositionClasses = 'right-0 top-full translate-y-4'
    const dropClasses = dropVisibleClasses + dropCommonClasses + dropPositionClasses + ''

    function onFocus() {
        setOpen(true)
    }

    function onUpdate(e: ChangeEvent<HTMLInputElement>) {
        if (!e.target?.files) {
            return
        }

        const file = e.target?.files[0]

        const reader = new FileReader();
        reader.onload = (event: any) => {
            props.setSrc(event.target.result)
        };
        reader.onerror = (event: any) => {
            console.log("File could not be read: " + event.target.error.code)
        };

        if (file) {
            reader.readAsDataURL(file);
            props.setFile(file)
        } else {
            props.setFile(undefined)
        }
    }

    useEffect(() => {
        function handleClickOutside(event: MouseEvent) {
            if (wrapperRef.current != null && !wrapperRef.current?.contains((event.target as unknown) as Node)) {
                setOpen(false)
            }
        }

        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [wrapperRef]);

    return <div className='relative'>
        <button className='h-16 w-16 rounded-full overflow-hidden' onFocus={onFocus}>
            <img className='w-full h-full' src={props.src} alt=''/>
        </button>
        <div className={dropClasses} ref={wrapperRef}>
            <label htmlFor='if-avatar' className='block cursor-pointer w-full text-left rounded-t-lg px-4 py-2 hover:bg-gray-100'>
                <span>Update avatar</span>
                <input id='if-avatar' type='file' onChange={onUpdate} className='w-full hidden'/>
            </label>
            <button onClick={props.onDelete} className='w-full text-left rounded-b-lg text-red-500 px-4 py-2 hover:bg-gray-100'>Delete avatar</button>
        </div>
    </div>
}

export default AccountPersonalPage