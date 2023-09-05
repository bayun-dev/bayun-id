import * as React from "react";
import {PropsWithChildren, ReactNode} from "react";

type HeaderProps = PropsWithChildren<{}>

const Header = (props: HeaderProps) => {
    return <>
        <header className='flex justify-center bg-white pt-4 '>
            <div className=' h-14 w-full px-4 lg:px-6 flex justify-between '>
                {props.children}
            </div>
        </header>

    </>
}

export default Header