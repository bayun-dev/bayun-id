import * as React from "react";
import Account from "../../api/objects/Account";
import Logo from "../../component/Logo";
import {useLoaderData} from "react-router";
import Header from "../../component/Header";
import {useEffect, useRef, useState} from "react";

const MePageHeader = () => {

    const me = useLoaderData() as Account

    return <Header>
        <a className='flex h-full items-center font-logo text-3xl' href='/'>
            <Logo/>
        </a>
        <div className='h-full flex justify-end items-center'>
            {/*<div className='h-10 w-10 rounded-full overflow-hidden'>*/}
            {/*    <img className='w-full h-full' src={`/avatar/${me.avatarId == null ? 'default' : me.avatarId}`} alt=''/>*/}
            {/*</div>*/}
            <Widget avatarSrc={`/avatar/${me.avatarId == null ? 'default' : me.avatarId}`}/>
        </div>
    </Header>
}

const Widget = (props: {avatarSrc: string}) => {

    const [open, setOpen] = useState<boolean>(false)

    const wrapperRef = useRef<HTMLDivElement | null>(null);

    const dropVisibleClasses = open ? '' : 'hidden '
    const dropPositionClasses = 'absolute right-0 top-full w-72 h-[277px] translate-y-4'
    const dropClasses = dropVisibleClasses + dropPositionClasses + ''

    function onFocus() {
        setOpen(true)
    }

    useEffect(() => {
        function handleClickOutside(event: MouseEvent) {
            if (wrapperRef.current != null && !wrapperRef.current?.contains((event.target as unknown) as Node)) {
                setOpen(false)
            }
        }

        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, [wrapperRef]);

    return <div className='relative'>
        <button className='focus:outline-none' tabIndex={-1} onFocus={onFocus}>
            <img className='w-10 h-10 rounded-full ring-2 ring-offset-2 ring-black' src={props.avatarSrc} alt=''/>
        </button>
        <div className={dropClasses} ref={wrapperRef}>
            <iframe className='w-full h-full' src='/widget'/>
        </div>
    </div>
}

export default MePageHeader