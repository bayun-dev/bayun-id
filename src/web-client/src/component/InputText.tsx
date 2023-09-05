import {ChangeEvent, PropsWithChildren, useState} from "react";
import {nextId} from "../utils/random";
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";
import * as React from "react";

type InputTextProps = {
    label?: string | undefined
    name?: string | undefined
    id?: string | undefined
    placeholder?: string | undefined
    type?: 'text' | 'password' | undefined
    size?: 'small' | 'normal' | 'large' | undefined
    disabled?: boolean | undefined
    autoFocus?: boolean | undefined
    errorMessage?: string | undefined
    value?: string | undefined
    defaultValue?: string | undefined
    color?: 'dark' | 'blue' | 'green' | 'red' | 'purple' | 'yellow' | undefined
    onChange?: (e: ChangeEvent<HTMLInputElement>) => void,
}


const InputText = (props: PropsWithChildren<InputTextProps>) => {

    // const [id] = useState<string>(props.id ? props.id : nextId('it-'))
    const id = props.id ? props.id : nextId('it-')

    function getType() {
        switch (props.type) {
            case 'password':
                return props.type
            default:
                return 'text'
        }
    }

    function getTextSize() {
        switch (props.size) {
            case 'small':
                return 'text-base'
            case 'large':
                return 'text-2xl'
            default:
                return 'text-xl'
        }
    }

    function getPadding() {
        switch (props.size) {
            case 'small':
                return 'p-2'
            case 'large':
                return 'p-4'
            default:
                return 'p-3'
        }
    }

    function getBorderColor() {
        let borderColor = 'border-gray-300 dark:border-gray-600 '
        switch(props.color) {
            case 'blue':
                borderColor += 'focus:border-blue-700 dark:focus:border-blue-700 focus:ring-blue-300 dark:focus:ring-blue-800'
                break
            case 'green':
                borderColor += 'focus:border-green-700 dark:focus:border-green-700 focus:ring-green-300 dark:focus:ring-green-800'
                break
            case 'red':
                borderColor += 'focus:border-red-700 dark:focus:border-red-700 focus:ring-red-300 dark:focus:ring-red-900'
                break
            case 'purple':
                borderColor += 'focus:border-purple-700 dark:focus:border-purple-700 focus:ring-purple-300 dark:focus:ring-purple-900'
                break
            case 'yellow':
                borderColor += 'focus:border-yellow-400 dark:focus:border-yellow-400 focus:ring-yellow-300 dark:focus:ring-yellow-900'
                break
            default:
                borderColor += 'focus:border-gray-700 dark:focus:border-gray-700 focus:ring-gray-300 dark:focus:ring-gray-900'
        }

        return props.errorMessage ? 'border-red-500 focus:ring-red-500 focus:border-red-500 dark:border-red-500' : borderColor
    }

    const iBorderClasses = 'border rounded-lg focus:ring-1 ' + getBorderColor()

    const iTextClasses = getTextSize() + (props.errorMessage ? ' text-red-900 placeholder-red-700 dark:text-red-500 dark:placeholder-red-500'
        : ' text-gray-900 dark:placeholder-gray-400 ' + (props.disabled ? 'dark:text-gray-400' : 'dark:text-white'))

    const iBgClasses = props.disabled ? 'bg-gray-100 dark:bg-gray-700' : props.errorMessage ? 'bg-red-50 dark:bg-gray-700' : 'bg-gray-50 dark:bg-gray-700'
    const iCommonClasses = getPadding() + ' block w-full outline-none'
    const iDisabledClasses = props.disabled ? 'cursor-not-allowed' : ''
    const iClasses = [iCommonClasses, iDisabledClasses, iTextClasses, iBgClasses, iBorderClasses].join(' ')

    const lClasses = 'block mb-2 text-base font-medium' + (props.errorMessage ? ' text-red-700 dark:text-red-500' : ' text-gray-900 dark:text-white')

    return <>
        <div className='w-full'>
            { props.label && <label htmlFor={id} className={lClasses}>{props.label}</label> }
            <input value={props.value} defaultValue={props.defaultValue} autoFocus={props.autoFocus} id={id} type={getType()} placeholder={props.placeholder} className={iClasses} disabled={props.disabled} onChange={props.onChange}/>
            { props.errorMessage && <p className="mt-2 text-sm text-red-600 dark:text-red-500">{props.errorMessage}</p> }
            { props.children && <div id="helper-text-explanation" className="mt-2 text-sm text-gray-500 dark:text-gray-400">{props.children}</div> }
        </div>
    </>
}

export default InputText