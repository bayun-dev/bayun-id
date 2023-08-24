import {nextId} from "../utils/random";
import {MouseEventHandler, PropsWithChildren} from "react";

type ButtonProps = {
    id?: string | undefined
    type?: 'dark' | 'light' | 'green' | 'red' | 'yellow' | 'blue' | 'purple' | undefined
    size?: 'small' | 'normal' | 'large'
    disabled?: boolean | undefined
    roundedFull?: boolean | undefined
    outline?: boolean | undefined
    onClick?: MouseEventHandler<HTMLButtonElement> | undefined
}

const Button = (props: PropsWithChildren<ButtonProps>) => {

    const id = props.id ? props.id : nextId('it-')

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

    function getBgColor() {
        switch (props.type) {
            case 'dark':
                return '' + (props.outline ? '' : ' bg-gray-800 dark:bg-gray-800') + (props.disabled ? '' : ' hover:bg-gray-900 dark:hover:bg-gray-700')
            case 'green':
                return '' + (props.outline ? '' : ' bg-green-700 dark:bg-green-600') + (props.disabled ? '' : ' hover:bg-green-800 dark:hover:bg-green-700')
            case 'red':
                return '' + (props.outline ? '' : ' bg-red-700 dark:bg-red-600') + (props.disabled ? '' : ' hover:bg-red-800 dark:hover:bg-red-700')
            case 'yellow':
                return '' + (props.outline ? '' : ' bg-yellow-400') + (props.disabled ? '' : ' hover:bg-yellow-500')
            case 'blue':
                return '' + (props.outline ? '' : ' bg-blue-700 dark:bg-blue-600') + (props.disabled ? '' : ' hover:bg-blue-800 dark:hover:bg-blue-700')
            case 'purple':
                return '' + (props.outline ? '' : ' bg-purple-700 dark:bg-purple-600') + (props.disabled ? '' : ' hover:bg-purple-800 dark:hover:bg-purple-700')
            default: // light or undefined
                return '' + (props.outline ? '' : ' bg-white dark:bg-gray-800') + (props.disabled ? '' : ' hover:bg-gray-100 dark:hover:bg-gray-700')
        }
    }

    function getBorderColor() {
        switch (props.type) {
            case 'dark':
                return 'focus:ring-gray-300 dark:focus:ring-gray-700 dark:border-gray-700' + (props.outline ? ' border border-gray-800' : '')
            case 'green':
                return 'focus:ring-green-300 dark:focus:ring-green-800' + (props.outline ? ' border border-green-700' : '')
            case 'red':
                return 'focus:ring-red-300 dark:focus:ring-red-900' + (props.outline ? ' border border-red-700' : '')
            case 'yellow':
                return 'focus:ring-yellow-300 dark:focus:ring-yellow-900' + (props.outline ? ' border border-yellow-400' : '')
            case 'blue':
                return 'focus:ring-blue-300 dark:focus:ring-blue-800' + (props.outline ? ' border border-blue-700' : '')
            case 'purple':
                return 'focus:ring-purple-300 dark:focus:ring-purple-900' + (props.outline ? ' border border-purple-700' : '')
            default: // light or undefined
                return 'border border-gray-300 focus:ring-gray-200 dark:border-gray-600 dark:hover:border-gray-600 dark:focus:ring-gray-700' + (props.outline ? ' border?' : '')
        }
    }

    function getTextColor() {
        switch (props.type) {
            case 'dark':
                return (props.outline ? 'text-gray-900 dark:text-gray-400'+(props.disabled ? '' : ' hover:text-white dark:hover:text-white') : 'text-white ')
            case 'green':
                return (props.outline ? 'text-green-700 dark:text-green-500'+(props.disabled ? '' : ' hover:text-white dark:hover:text-white'): 'text-white')
            case 'red':
                return (props.outline ? 'text-red-700 dark:text-red-500'+(props.disabled ? '' : ' hover:text-white dark:hover:text-white'): 'text-white')
            case 'yellow':
                return (props.outline ? 'text-yellow-400 dark:text-yellow-300'+(props.disabled ? '' : ' hover:text-white dark:hover:text-white'): 'text-white')
            case 'blue':
                return (props.outline ? 'text-blue-700 dark:text-blue-500'+(props.disabled ? '' : ' hover:text-white dark:hover:text-white'): 'text-white')
            case 'purple':
                return (props.outline ? 'text-purple-700 dark:text-purple-400'+(props.disabled ? '' : ' hover:text-white dark:hover:text-white'): 'text-white')
            default: // light or undefined
                return 'text-black dark:text-white'
        }
    }

    function getPadding() {
        switch (props.size) {
            case 'small':
                return 'px-3 py-2'
            case 'large':
                return 'px-5 py-3'
            default:
                return 'px-5 py-2.5'
        }
    }

    const bBorderClasses = (props.roundedFull ? 'rounded-full':'rounded-lg') + ' focus:ring-4 focus:outline-none ' + getBorderColor()
    const bBgClasses = getBgColor()
    const bTextClasses = [getTextSize(), getTextColor(), 'font-medium text-center'].join(' ')
    const bCommonClasses = [getPadding(), 'w-full'].join(' ')
    const bDisabledClasses = props.disabled ? 'cursor-not-allowed' : ''
    const bClasses = [bCommonClasses, bDisabledClasses, bBorderClasses, bBgClasses, bTextClasses].join(' ')

    return <>
        <button id={id} className={bClasses} disabled={props.disabled} onClick={props.onClick}>
            {props.children}
        </button>
    </>
}

export default Button