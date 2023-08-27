import {useRouteError} from "react-router-dom";
import Exception, {IException} from "../../utils/Exception";
import {createContext, PropsWithChildren, ReactNode, useContext, useState} from "react";
import {IErrorCode} from "../../api/schemas/IError";

const enum ErrorElementTitle {
    FORBIDDEN = 'Error 403: Forbidden',
    INTERNAL = 'Error 500: Internal',
    UNKNOWN = 'Unknown Error'
}

const enum ErrorElementCode {
    FORBIDDEN = '403',
    INTERNAL = '500',
    UNKNOWN = 'XXX'
}

const ErrorPage = (props: {exception?: IException}) => {
    const exception = useRouteError() as Exception

    const element = getSpecificErrorElement(exception.code)

    return <>
        <div className='h-full flex justify-center items-center bg-white '>
            <div className='w-[500px] text-center'>
                <div>
                    <span className='text-3xl font-normal'>{element.title}</span>
                </div>
                <div className='my-8'>
                    <span className='text-9xl text-red-500'>{element.code}</span>
                </div>
                <div className='text-xl text-gray-500'>{element.description}</div>
            </div>
        </div>
    </>

}

type IErrorElement = {
    title: ReactNode,
    code: ReactNode,
    description?: ReactNode
}

const getSpecificErrorElement = (code: string): IErrorElement => {
    console.log('code', code)
    switch (code) {
        case IErrorCode.ACCOUNT_BLOCKED: return {
            title: ErrorElementTitle.FORBIDDEN,
            code: ErrorElementCode.FORBIDDEN,
            description: <>
                Your account has been blocked.
            </>
        }
        case IErrorCode.ACCOUNT_DELETED: return {
            title: ErrorElementTitle.FORBIDDEN,
            code: ErrorElementCode.FORBIDDEN,
            description: <>
                Your account has been deleted.
            </>
        }
        case IErrorCode.INTERNAL: return {
            title: ErrorElementTitle.INTERNAL,
            code: ErrorElementCode.INTERNAL,
            description: <>
                Looks like something went wrong. We will fix this soon.
            </>
        }
        default: return {
            title: ErrorElementTitle.UNKNOWN,
            code: ErrorElementCode.UNKNOWN,
            description: <>
                Looks like something went wrong. We will fix this soon.
            </>
        }
    }
}

export type ErrorPageContext = {
    throws: (e: IException) => void
} | undefined

const ErrorPageContext = createContext<ErrorPageContext>(undefined)

export const useErrorPage = (): ErrorPageContext => {
    const context = useContext<ErrorPageContext>(ErrorPageContext)
    if (!context) {
        throw 'No ErrorPageProvider found when calling useErrorPage.'
    }

    return context
}

export const ErrorPageProvider = (props: PropsWithChildren) => {

    const [exception, setException] = useState<IException | undefined>();

    function throws(e: Exception) {
        setException(e)
    }

    return <ErrorPageContext.Provider value={{throws}}>
        { exception ? <>
            <ErrorPage exception={exception}/>
        </> : <>
            {props.children}
        </>}
    </ErrorPageContext.Provider>
}

export default ErrorPage