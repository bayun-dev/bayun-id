import {createContext, PropsWithChildren, ReactNode, useContext, useState} from "react";

export type ModalContextType = {
    open: (element: ReactNode) => void
    close: () => void
}

const ModalContext = createContext<ModalContextType | undefined>(undefined)

const ModalWrapper = (props: { element: ReactNode }) => {
    return <div className=''>
        {props.element}
    </div>
}

export const ModalProvider = (props: PropsWithChildren) => {

    const [isOpen, setIsOpen] = useState<boolean>(false)
    const [element, setElement] = useState<ReactNode | undefined>(undefined)

    function open(element: ReactNode) {
        setElement(element)
        setIsOpen(true)
    }

    function close() {
        setIsOpen(false)
        setElement(undefined)
    }

    return <ModalContext.Provider value={{open, close}}>
        {isOpen ? <ModalWrapper element={element}/> : props.children}
    </ModalContext.Provider>
}

const useModal = (): ModalContextType => {
    const context = useContext<ModalContextType | undefined>(ModalContext)
    if (!context) {
        throw new Error('No ModalProvider found when calling useModal.')
    }

    return context
}

export default useModal