import React, {
    createContext,
    Dispatch,
    PropsWithChildren,
    ReactNode,
    SetStateAction,
    useContext,
    useState
} from "react";
import ErrorPageWrapper from "../pages/error/ErrorPageWrapper";
import InternalErrorPage from "../pages/error/InternalErrorPage"

export type ErrorPageContextType = {
    setErrorElement: Dispatch<SetStateAction<ReactNode | undefined>>
}

const ErrorPageContext = createContext<ErrorPageContextType | undefined>(undefined)

export const ErrorPageProvider = (props: PropsWithChildren) => {

    const [errorElement, setErrorElement] = useState<ReactNode | undefined>(undefined)

    return <ErrorPageContext.Provider value={{setErrorElement}}>
        { errorElement == undefined ? props.children : <ErrorPageWrapper errorElement={errorElement}/>}
    </ErrorPageContext.Provider>
}

const useErrorPage = () => {
    const context = useContext<ErrorPageContextType | undefined>(ErrorPageContext)
    if (!context) {
        throw new Error('No ErrorPageProvider found when calling useErrorPage.')
    }

    return {
        show: {
            internalError: () => context.setErrorElement(<InternalErrorPage/>)
        }
    }
}

export default useErrorPage