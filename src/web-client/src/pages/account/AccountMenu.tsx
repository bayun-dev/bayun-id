import {ReactNode} from "react";
import {NavLink} from "react-router-dom";
import {SvgHome, SvgIdentification, SvgShieldCheck} from "../../component/svg";

const AccountMenu = () => {
    return <>
        <div className='flex flex-col bg-white border border-gray-200 shadow-sm rounded-lg py-2'>
            <NavItem label='Home' icon={<SvgHome className='w-7 h-7'/>} to='/'/>
            <NavItem label='Personal info' icon={<SvgIdentification className='w-7 h-7'/>} to='/personal'/>
            <NavItem label='Security' icon={<SvgShieldCheck className='w-7 h-7'/>} to='/security'/>
        </div>
    </>
}

type NavItemProps = {
    label: string
    to: string
    icon: ReactNode
}
const NavItem = (props: NavItemProps) => {

    const currentPageClasses = 'aria-[current=page]:bg-gray-100 aria-[current=page]:pointer-events-none'
    const commonClasses = 'flex h-full items-center gap-4 p-4 hover:bg-gray-100'
    const navClasses = [commonClasses, currentPageClasses].join(' ')

    return <>
        <NavLink className={navClasses} to={props.to}>
            <div>{props.icon}</div>
            <div>
                <span>{props.label}</span>
            </div>
        </NavLink>
    </>
}

export default AccountMenu