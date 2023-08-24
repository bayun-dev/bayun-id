import {ChangeEvent, PropsWithChildren, ReactNode} from "react";
import {nextId} from "../utils/random";

type CheckboxProps = {
    id?: string | undefined
    value?: boolean | undefined
    disabled?: boolean | undefined
    label?: string | ReactNode | undefined
    color?: 'dark' | 'green' | 'red' | 'yellow' | 'blue' | 'purple'
    onChange?: (e: ChangeEvent<HTMLInputElement>) => void
}

const Checkbox = (props: PropsWithChildren<CheckboxProps>) => {

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

    const iBorderClasses = 'rounded focus:ring-2 focus:ring-offset-white dark:focus:ring-offset-black ' + getIBorderColor()
    const iTextClasses = getITextColor()
    const iCommonClasses = ['w-4 h-4'].join(' ')
    const iDisabledClasses = props.disabled ? 'cursor-not-allowed' : 'cursor-pointer'
    const iClasses = [iCommonClasses, iDisabledClasses, iTextClasses, iBorderClasses].join(' ')

    const lTextClasses = props.disabled ? 'text-gray-400 dark:text-gray-500' : 'text-gray-900 dark:text-gray-300'
    const lCommonClasses = 'font-medium'
    const lDisabledClasses = ''
    const lClasses = [lCommonClasses, lDisabledClasses, lTextClasses].join(' ')

    return <>
        <div className="flex items-center">
            <div className="flex items-center h-5">
                <input id={id} type="checkbox" disabled={props.disabled} onChange={props.onChange} checked={props.value} className={iClasses}/>
            </div>
            <div className="ml-2">
                { props.label && <label htmlFor={id} className={lClasses}>{props.label}</label> }
                { props.children && <p className="text-xs font-normal text-gray-500 dark:text-gray-300">{ props.children }</p> }
            </div>
        </div>
    </>
}

export default Checkbox