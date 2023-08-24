import {ChangeEvent, PropsWithChildren, ReactNode} from "react";
import {nextId} from "../utils/random";
import * as React from "react";

type ToggleProps = {
    id?: string | undefined
    value?: boolean | undefined
    disabled?: boolean | undefined
    label?: string | ReactNode | undefined
    size?: 'small' | 'normal' | 'large' | undefined
    color?: 'dark' | 'green' | 'red' | 'yellow' | 'blue' | 'purple'
    errorMessage?: string | undefined
    prefix?: string | undefined
    suffix?: string | undefined
    onChange?: (e: ChangeEvent<HTMLInputElement>) => void
}

const Toggle = (props: PropsWithChildren<ToggleProps>) => {

    const id = props.id ? props.id : nextId('cb-')

    function getITextColor() {
        switch (props.color) {
            case 'dark':
                return 'text-gray-800'
            case 'green':
                return 'text-green-600'
            case 'red':
                return 'text-red-600'
            case 'yellow':
                return 'text-yellow-400'
            case 'purple':
                return 'text-purple-600'
            default: // blue or undefined
                return 'text-blue-600'
        }
    }

    function getIBorderColor() {
        switch (props.color) {
            case 'dark':
                return 'focus:ring-gray-700 dark:focus:ring-gray-800'
            case 'green':
                return 'focus:ring-green-500 dark:focus:ring-green-600'
            case 'red':
                return 'focus:ring-red-500 dark:focus:ring-red-600'
            case 'yellow':
                return 'focus:ring-yellow-500 dark:focus:ring-yellow-600'
            case 'purple':
                return 'focus:ring-purple-500 dark:focus:ring-purple-600'
            default: // blue or undefined
                return 'focus:ring-blue-500 dark:focus:ring-blue-600'
        }
    }

    function getDBgColor() {
        let bgColor = 'bg-gray-200 dark:bg-gray-700 '
        switch (props.color) {
            case 'dark':
                bgColor += 'peer-focus:ring-gray-300 dark:peer-focus:ring-gray-800 peer-checked:bg-gray-600'
                break;
            case 'green':
                bgColor += 'peer-focus:ring-green-300 dark:peer-focus:ring-green-800 peer-checked:bg-green-600'
                break;
            case 'red':
                bgColor += 'peer-focus:ring-red-300 dark:peer-focus:ring-red-800 peer-checked:bg-red-600'
                break;
            case 'yellow':
                bgColor += 'peer-focus:ring-yellow-300 dark:peer-focus:ring-yellow-800 peer-checked:bg-yellow-400'
                break;
            case 'purple':
                bgColor += 'peer-focus:ring-purple-300 dark:peer-focus:ring-purple-800 peer-checked:bg-purple-600'
                break;
            default: // blue or undefined
                bgColor += 'peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 peer-checked:bg-blue-600'
        }
        return bgColor.trim()
    }

    function getDSize() {
        switch (props.size) {
            case 'small':
                return 'w-9 h-5 after:top-0.5 after:left-0.5 after:h-4 after:w-4 peer-checked:after:translate-x-full'
            case 'large':
                return 'w-14 h-7 after:top-0.5 after:left-0.5 after:h-6 after:w-6 peer-checked:after:translate-x-7'
            default:
                return 'w-11 h-6 after:top-0.5 after:left-0.5 after:h-5 after:w-5 peer-checked:after:translate-x-full'
        }
    }

    const iBorderClasses = 'rounded focus:ring-2 focus:ring-offset-white dark:focus:ring-offset-black ' + getIBorderColor()
    const iTextClasses = getITextColor()
    const iCommonClasses = ['w-4 h-4 sr-only peer'].join(' ')
    const iDisabledClasses = props.disabled ? 'cursor-not-allowed' : 'cursor-pointer'
    const iClasses = [iCommonClasses, iDisabledClasses, iTextClasses, iBorderClasses].join(' ')

    function getLTextSize() {
        return 'text-sm '
    }

    const fixTextClasses = getLTextSize() + (props.disabled ? 'text-gray-400 dark:text-gray-500' : 'text-gray-900 dark:text-gray-300')
    const fixCommonClasses = ''
    const fixDisabledClasses = ''
    const fixClasses = [fixCommonClasses, fixDisabledClasses, fixTextClasses].join(' ')

    const dBgClasses = '' + getDBgColor()
    const dCommonClasses = ['relative rounded-full peer peer-focus:ring-4',
        'peer-checked:after:border-white after:content-[\'\'] after:absolute after:bg-white after:border-gray-300',
        'after:border after:rounded-full after:transition-all dark:border-gray-600'].join(' ')
    const dDisabledClasses = ''
    const dClasses = [dCommonClasses, dDisabledClasses, dBgClasses, getDSize()].join(' ')

    const lClasses = 'block mb-2 text-base font-medium' + (props.errorMessage ? ' text-red-700 dark:text-red-500' : ' text-gray-900 dark:text-white')
    const prefixClasses = ['mr-3', fixClasses].join(' ')
    const suffixClasses = ['ml-3', fixClasses].join(' ')

    return <>
        <div>
            {props.label && <div className={lClasses}>
                <span>{props.label}</span>
            </div> }
            <label className="relative inline-flex items-center cursor-pointer">
                <span className={prefixClasses}>{props.prefix}</span>
                <input type="checkbox" value="" className={iClasses} checked={props.value} onChange={props.onChange} disabled={props.disabled}/>
                <div className={dClasses}></div>
                <span className={suffixClasses}>{props.suffix}</span>
            </label>
        </div>
    </>
}

export default Toggle