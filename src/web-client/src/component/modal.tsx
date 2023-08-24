import React, {createContext, PropsWithChildren, ReactNode, useCallback, useContext, useState} from "react";
import {SvgXMark} from "./svg";

export function Modal(props: {isOpen: boolean, title: string, node: ReactNode}) {

    const modal = useModal()

    return props.isOpen ? <div className='fixed inset-0 bg-slate-600 bg-opacity-80 flex flex-col justify-center items-center'>
        <div className='relative flex flex-col w-1/2 bg-white rounded-xl p-8'>
            {/*header*/}
            <div className='flex justify-between items-center pb-8'>
                <div>
                    <span className='text-2xl'>{props.title}</span>
                </div>
                <button className='btn-white p-2' onClick={modal.close}>
                    <SvgXMark className='mx-auto stroke-2 w-6 h-6'/>
                </button>
            </div>
            { props.node }
        </div>

    </div> : <></>
}

const ModalContext = createContext({
    open: (title: string, n: ReactNode) => {},
    close: () => {}
})

export function useModal()  {
    return useContext(ModalContext)
}

export function ModalProvider(props: PropsWithChildren) {
    const [isOpen, setIsOpen] = useState<boolean>(false)
    const [title, setTitle] = useState<string>('')
    const [node, setNode] = useState<ReactNode>(<></>)

    const open = useCallback((t: string, n: ReactNode) => {
        setIsOpen(true)
        setTitle(t)
        setNode(n)
    }, [])

    const close = useCallback(() => {
        setIsOpen(false)
        setTitle('')
        setNode(<></>)
    }, [])

    return <ModalContext.Provider value={{ open, close }}>
        <Modal isOpen={isOpen} node={node} title={title}/>
        {props.children}
    </ModalContext.Provider>
}

export default Modal

