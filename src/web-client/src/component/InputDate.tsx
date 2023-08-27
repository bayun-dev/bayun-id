import {
    ChangeEvent,
    forwardRef,
    HTMLAttributes,
    InputHTMLAttributes, LegacyRef,
    PropsWithChildren,
    useEffect,
    useState
} from "react";
import {nextId} from "../utils/random";
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";
import "./InputDate.css";
import * as React from "react";
import InputText from "./InputText";

type InputDateProps = {
    label?: string | undefined
    name?: string | undefined
    id?: string | undefined
    placeholder?: string | undefined
    size?: 'small' | 'normal' | 'large' | undefined
    disabled?: boolean | undefined
    dateFormat?: string | undefined
    autoFocus?: boolean | undefined
    errorMessage?: string | undefined
    selected?: Date | null
    color?: 'dark' | 'blue' | 'green' | 'red' | 'purple' | 'yellow' | undefined
    onChange(
        date: Date | null,
        event: React.SyntheticEvent<any> | undefined,
    ): void;
    minDate?: Date | undefined
    maxDate?: Date | undefined
}


const InputDate = (props: PropsWithChildren<InputDateProps>) => {

    // const [id] = useState<string>(props.id ? props.id : nextId('it-'))
    const id = props.id ? props.id : nextId('id-')
    const currentDate = new Date()

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

    const CustomInput = forwardRef(({ value, onClick }: any, ref: LegacyRef<HTMLInputElement>) =>
            <div>
                {props.label && <label htmlFor={id} className={lClasses}>{props.label}</label>}
                <input onClick={onClick} value={value} ref={ref} readOnly={true} type='text' placeholder={props.placeholder}
                       className={iClasses} disabled={props.disabled}/>
                {props.errorMessage && <p className="mt-2 text-sm text-red-600 dark:text-red-500">{props.errorMessage}</p>}
                {props.children && <p id="helper-text-explanation"
                                      className="mt-2 text-sm text-gray-500 dark:text-gray-400">{props.children}</p>}
            </div>
    );

    function getDateClasses(date: Date) {
        switch(props.color) {
            case 'blue':
                return 'aria-selected:bg-blue-600 aria-selected:hover:bg-blue-800'
            case 'green':
                return 'aria-selected:bg-green-600 aria-selected:hover:bg-green-800'
            case 'red':
                return 'aria-selected:bg-red-600 aria-selected:hover:bg-red-800'
            case 'yellow':
                return 'aria-selected:bg-yellow-500 aria-selected:hover:bg-yellow-700'
            case 'purple':
                return 'aria-selected:bg-purple-600 aria-selected:hover:bg-purple-800'
            default: // dark or undefined
                return 'aria-selected:bg-gray-600 aria-selected:hover:bg-gray-800'
        }
    }

    return <>
        <div>
            <DatePicker selected={props.selected} onChange={props.onChange} className={iClasses}
                        popperPlacement='top'
                        showYearDropdown={true}
                        showMonthDropdown={true}
                        dropdownMode='select'
                        disabledKeyboardNavigation={true}
                        minDate={ props.minDate ? props.minDate : new Date(1900, 0,1)}
                        maxDate={ props.maxDate ? props.maxDate : currentDate}
                        dayClassName={date => {
                            return getDateClasses(date)
                        }}
                        customInput={<CustomInput/>}
                        placeholderText={props.placeholder} dateFormat='dd.MM.yyyy'/>
        </div>
    </>
}

export default InputDate