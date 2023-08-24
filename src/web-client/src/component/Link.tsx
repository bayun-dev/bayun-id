import {PropsWithChildren} from "react";

type LinkProps = {
    color?: 'dark' | 'blue' | 'green' | 'red' | 'yellow' | 'purple' | undefined
    href?: string | undefined
    navigate?: (to: string) => void | undefined
    locationReplace?: boolean | undefined
}

const Link = (props: PropsWithChildren<LinkProps>) => {

    function onClick() {
        if (props.href) {
            if (props.navigate && props.href) {
                props.navigate(props.href)
            } else {
                if (props.locationReplace) {
                    window.location.href = props.href
                } else {
                    window.location.replace(props.href)
                }
            }
        }
    }

    function getTextColor() {
        switch (props.color) {
            case 'dark':
                return 'text-black dark:text-white'
            case 'green':
                return 'text-green-600 dark:text-green-500'
            case 'red':
                return 'text-red-600 dark:text-red-500'
            case 'yellow':
                return 'text-yellow-500 dark:text-yellow-500'
            case 'purple':
                return 'text-purple-600 dark:text-purple-500'
            default: // blue & undefined
                return 'text-blue-600 dark:text-blue-500'
        }
    }

    function getBorderColor() {
        switch (props.color) {
            case 'dark':
                return 'focus:outline-black dark:focus:outline-white'
            case 'green':
                return 'focus:outline-green-600 dark:focus:outline-green-500'
            case 'red':
                return 'focus:outline-red-600 dark:focus:outline-red-500'
            case 'yellow':
                return 'focus:outline-yellow-500 dark:focus:outline-yellow-500'
            case 'purple':
                return 'focus:outline-purple-600 dark:focus:outline-purple-500'
            default: // blue & undefined
                return 'focus:outline-blue-600 dark:focus:outline-blue-500'
        }
    }

    const aBorderClasses = '' + getBorderColor()
    const aTextClasses = ['font-medium hover:underline', getTextColor()].join(' ')
    const aCommonClasses = 'cursor-pointer'
    const aClasses = [aCommonClasses, aTextClasses, aBorderClasses].join(' ')

    return <a href='#' className={aClasses} onClick={onClick}>
        {props.children}
    </a>
}

export default Link