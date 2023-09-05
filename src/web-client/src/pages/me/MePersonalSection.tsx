import Account from "../../api/objects/Account";
import Button from "../../component/Button";
import * as React from "react";
import {ChangeEvent, useEffect, useRef, useState} from "react";
import {SvgPencil, SvgTrash} from "../../component/Svg";
import InputText from "../../component/InputText";
import * as NameValidator from "../../api/validation/NameValidator";
import * as AvatarValidator from "../../api/validation/AvatarValidator";
import MethodMeSave from "../../api/methods/MethodMeSave";
import SwapSection, {SwapElementProps} from "../../component/SwapSection";
import {useLoaderData} from "react-router";
import {AxiosError} from "axios";
import ErrorBody, {ErrorType} from "../../api/objects/ErrorBody";
import useErrorPage from "../../hooks/useErrorPage";


const MePersonalSwapElement = (props: SwapElementProps) => {

    const me = useLoaderData() as Account
    const errorPage = useErrorPage()

    const [avatarSrc, setAvatarSrc] = useState<string>('/avatar/' + (me.avatarId === null ? 'default' : me.avatarId))
    const [avatarError, setAvatarError] = useState<string | undefined>(undefined)
    const [avatarFile, setAvatarFile] = useState<File | undefined>(undefined)
    const [isDropAvatar, setIsDropAvatar] = useState<boolean>(false)
    const avatarCanvasRef = useRef<HTMLCanvasElement>(null)
    function onUploadAvatarFile(e: ChangeEvent<HTMLInputElement>) {
        if (!e.target || !e.target.files) {
            return;
        }

        const file = e.target?.files[0]
        if (!file) {
            return
        }

        const validationResult = AvatarValidator.inlineValidate(file)
        setAvatarError(validationResult)
        if (validationResult) {
            return
        }

        const reader = new FileReader();
        reader.onload = (e: ProgressEvent<FileReader>) => {
            const img = new Image()
            const canvas = avatarCanvasRef.current!
            img.onload = () => {
                const side = Math.min(img.width, img.height);
                const left = (img.width - side) / 2;
                const top = (img.height - side) / 2;
                const ctx = canvas.getContext("2d");
                if (ctx) {
                    ctx.drawImage(img, left, top, side, side, 0, 0, canvas.width, canvas.height);
                    setAvatarSrc(ctx.canvas.toDataURL())
                    setIsDropAvatar(false)
                }
            }
            img.src = e.target?.result as string
        };
        reader.onerror = (event: any) => {
            console.log("File could not be read: " + event.target.error.code)
        };
        reader.readAsDataURL(file);
        setAvatarFile(file)
    }
    function onDeleteAvatar() {
        setIsDropAvatar(true)
        setAvatarSrc('/avatar/default')
        setAvatarFile(undefined)
    }

    const [firstName, setFirstName] = useState<string>(me.firstName)
    const [firstNameError, setFirstNameError] = useState<string | undefined>(undefined)
    function onChangeFirstName(e: ChangeEvent<HTMLInputElement>) {
        const value = e.currentTarget.value
        setFirstName(value)
        setFirstNameError(NameValidator.inlineValidate(value))
    }

    const [lastName, setLastName] = useState<string>(me.lastName)
    const [lastNameError, setLastNameError] = useState<string | undefined>(undefined)
    function onChangeLastName(e: ChangeEvent<HTMLInputElement>) {
        const value = e.currentTarget.value
        setLastName(value)
        setLastNameError(NameValidator.inlineValidate(value))
    }

    const [disabledSaveButton, setDisabledSaveButton] = useState<boolean>(true)

    async function onSave() {
        const firstNameValidationResult = NameValidator.fullValidate(firstName);
        const lastNameValidationResult = NameValidator.fullValidate(lastName);

        if (firstNameValidationResult || lastNameValidationResult) {
            setFirstNameError(firstNameValidationResult)
            setLastNameError(lastNameValidationResult)
            return
        }

        setDisabledSaveButton(true)

        const method = new MethodMeSave({
            firstName, lastName,
            dropAvatar: isDropAvatar,
            avatar: avatarFile
        })
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
                    if (parameter === 'firstName') {
                        setFirstNameError('Invalid')
                    } else if (parameter === 'lastName') {
                        setLastNameError('Invalid')
                    } else if (parameter === 'avatar') {
                        setAvatarError('Invalid')
                    } else {
                        // ignore
                    }
                })
            } else {
                errorPage.show.internalError()
            }
        })
    }

    useEffect(() => {
        setDisabledSaveButton(
            firstName === me.firstName
            && lastName === me.lastName
            && avatarFile === undefined
            && !isDropAvatar
        )
    }, [avatarFile, firstName, lastName, isDropAvatar])

    return <>
        <div>
            <div className='w-full h-px bg-gray-200'></div>
            {/*avatar*/}
            <div className='grid grid-cols-3 w-full py-4'>
                <div className='grid place-content-center'>
                    <label htmlFor='if-avatar' className='bg-gray-100 hover:bg-gray-200 p-4 rounded-full cursor-pointer'>
                        <SvgPencil className='w-8 h-8'/>
                        <input id='if-avatar' type='file' accept={'image/png,image/jpg,image/jpeg'} onChange={onUploadAvatarFile} className='hidden'/>
                    </label>
                </div>
                <div className='grid place-content-center'>
                    <div className='w-32 h-32 rounded-full overflow-hidden border border-gray-200'>
                        <canvas ref={avatarCanvasRef} className='w-full h-full hidden'/>
                        <img className='w-full h-full' src={avatarSrc} alt=''/>
                    </div>
                </div>
                <div className='grid place-content-center'>
                    <button disabled={avatarSrc.endsWith('default')} className='disabled:cursor-not-allowed bg-gray-100 hover:bg-gray-200 p-4 rounded-full' onClick={onDeleteAvatar}>
                        <SvgTrash className='w-8 h-8'/>
                    </button>
                </div>
                <div className='col-span-3 text-center'>
                    { avatarError && <p className="mt-2 text-sm text-red-600 dark:text-red-500">{avatarError}</p> }
                </div>
            </div>
            <div className='w-full h-px bg-gray-200'></div>
            {/*name*/}
            <div className='p-4 space-y-4'>
                <InputText placeholder='First name' label='First name'
                           value={firstName} onChange={onChangeFirstName} errorMessage={firstNameError}/>
                <InputText placeholder='Last name' label='Last name'
                           value={lastName} onChange={onChangeLastName} errorMessage={lastNameError}/>
                <div className='pt-2 grid grid-cols-2 gap-4'>
                    <Button type='light' onClick={props.swap}>Cancel</Button>
                    <Button type='dark' onClick={onSave} disabled={disabledSaveButton}>Save</Button>
                </div>
            </div>
        </div>
    </>
}

const MePersonalSection = () => {

    const me = useLoaderData() as Account
    const [swap, setSwap] = useState<boolean>(false)

    return <>
        <SwapSection title='Basic info' description='This information is visible to other people using Bayun services.'
                     swapElement={<MePersonalSwapElement swap={() => setSwap(false)}/>} swap={swap} onClick={() => {setSwap(true)}}>
            <div className='flex gap-4'>
                <div className='grid place-content-center '>
                    <div className='rounded-full w-16 h-16 border border-gray-200 shadow-sm overflow-hidden'>
                        <img className='w-full h-full' src={`/avatar/${me.avatarId == null ? 'default' : me.avatarId}`} alt=''/>
                    </div>
                </div>
                <div className='my-auto -space-y-1.5'>
                    <div>
                        <span className='font-medium text-lg'>{me.firstName} {me.lastName}</span>
                    </div>
                    <div>
                        <span className='text-gray-500'>@{me.username}</span>
                    </div>
                </div>
            </div>
        </SwapSection>
    </>
}

export default MePersonalSection