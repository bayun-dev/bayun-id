import {createContext, PropsWithChildren, ReactNode, useContext, useState} from "react";
import Link from "../../component/Link";
import {isRouteErrorResponse, useRouteError} from "react-router-dom";

export type SignupErrorContextType = {
    oops: () => void
}

const SignupErrorContext = createContext<SignupErrorContextType | undefined>(undefined)

export const SignupErrorProvider = (props: PropsWithChildren) => {

    const [isError, setIsError] = useState<boolean>(false)
    const [errorContent, setErrorContent] = useState<ReactNode | undefined>(undefined)

    const oops = () => {
        setIsError(true)
        setErrorContent(<OopsErrorContent/>)
    }

    return <SignupErrorContext.Provider value={{
        oops: oops,
    }}>
        { isError ? <SignupErrorPage>{errorContent}</SignupErrorPage> : props.children}
    </SignupErrorContext.Provider>
}

export const SignupErrorElement = () => {

    const error = useRouteError()

    return <>
        <SignupErrorPage>
            { isRouteErrorResponse(error) && error.status === 404 ? <NotFoundErrorContent/> : <OopsErrorContent/> }
        </SignupErrorPage>
    </>
}

export const SignupErrorPage = (props: PropsWithChildren) => {
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
            Sorry, we can't find that page.<br/><Link href='/signup'>Back to sign up</Link>
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
            We are already working to solve the problem.<br/><Link href='/signup'>Back to sign up</Link>
        </p>
    </>
}

export const useSignupError = (): SignupErrorContextType => {
    const context = useContext<SignupErrorContextType | undefined>(SignupErrorContext)
    if (!context) {
        throw new Error(
            'No SignupErrorProvider not found when calling useSignupError.'
        );
    }

    return context
}