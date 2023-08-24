import {createContext, PropsWithChildren, ReactNode, useContext, useState} from "react";
import Link from "../../component/Link";
import {isRouteErrorResponse, useRouteError} from "react-router-dom";

export type LoginErrorContextType = {
    oops: () => void
    blocked: () => void
    deleted: () => void
}

const LoginErrorContext = createContext<LoginErrorContextType | undefined>(undefined)

export const LoginErrorProvider = (props: PropsWithChildren) => {

    const [isError, setIsError] = useState<boolean>(false)
    const [errorContent, setErrorContent] = useState<ReactNode | undefined>(undefined)

    const oops = () => {
        setIsError(true)
        setErrorContent(<OopsErrorContent/>)
    }

    const blocked = () => {
        setIsError(true)
        setErrorContent(<BlockedErrorContent/>)
    }

    const deleted = () => {
        setIsError(true)
        setErrorContent(<DeletedErrorContent/>)
    }

    return <LoginErrorContext.Provider value={{
        oops: oops,
        blocked: blocked,
        deleted: deleted
    }}>
        { isError ? <LoginErrorPage>{errorContent}</LoginErrorPage> : props.children}
    </LoginErrorContext.Provider>
}

export const LoginErrorElement = () => {

    const error = useRouteError()

    return <>
        <LoginErrorPage>
            { isRouteErrorResponse(error) && error.status === 404 ? <NotFoundErrorContent/> : <OopsErrorContent/> }
        </LoginErrorPage>
    </>
}

export const LoginErrorPage = (props: PropsWithChildren) => {
    return <>
        <div className="bg-gray-50 dark:bg-gray-900 min-h-screen flex flex-col w-full justify-center items-center text-center py-8 px-4">
            {props.children}
        </div>
    </>
}

const NotFoundErrorContent = () => {
    return <>
        <h1 className="mb-4 text-7xl tracking-tight font-extrabold lg:text-9xl text-blue-600 dark:text-primary-500">
            404
        </h1>
        <p className="mb-4 text-3xl tracking-tight font-bold text-gray-900 md:text-4xl dark:text-white">
            Something's missing.
        </p>
        <p className="mb-4 text-lg font-light text-gray-500 dark:text-gray-400">
            Sorry, we can't find that page.<br/><Link href='/login'>Back to log in</Link>
        </p>
    </>

}

export const OopsErrorContent = () => {
    return <>
        <h1 className="mb-4 text-7xl tracking-tight font-extrabold lg:text-9xl text-blue-600 dark:text-primary-500">
            OOPS
        </h1>
        <p className="mb-4 text-3xl tracking-tight font-bold text-gray-900 md:text-4xl dark:text-white">
            Look likes, somethings went wrong.
        </p>
        <p className="mb-4 text-lg font-light text-gray-500 dark:text-gray-400">
            We are already working to solve the problem.<br/><Link href='/login'>Back to log in</Link>
        </p>
    </>
}

const BlockedErrorContent = () => {
    return <>
        <h1 className="mb-4 text-7xl tracking-tight font-extrabold lg:text-9xl text-blue-600 dark:text-primary-500">
            ACCOUNT BLOCKED
        </h1>
        <p className="mb-4 text-3xl tracking-tight font-bold text-gray-900 md:text-4xl dark:text-white">
            Your account has been blocked, we're sorry.
        </p>
        <p className="mb-4 text-lg font-light text-gray-500 dark:text-gray-400">
            <Link href='/login'>Back to log in</Link>
        </p>
    </>
}

const DeletedErrorContent = () => {
    return <>
        <h1 className="mb-4 text-7xl tracking-tight font-extrabold lg:text-9xl text-blue-600 dark:text-primary-500">
            ACCOUNT DELETED
        </h1>
        <p className="mb-4 text-3xl tracking-tight font-bold text-gray-900 md:text-4xl dark:text-white">
            Recovering a deleted account is not possible, we're sorry.
        </p>
        <p className="mb-4 text-lg font-light text-gray-500 dark:text-gray-400">
            <Link href='/login'>Back to log in</Link>
        </p>
    </>
}

export const useLoginError = (): LoginErrorContextType => {
    const context = useContext<LoginErrorContextType | undefined>(LoginErrorContext)
    if (!context) {
        throw new Error(
            'No LoginErrorProvider not found when calling useLoginError.'
        );
    }

    return context
}