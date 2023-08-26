import logo from "../../assets/logo_64x64.png";
import {NavLink, useLocation, useMatches} from "react-router-dom";

const AccountHeader = () => {

    const matches = useMatches();

    function getHeader() {
        if (matches.some(value => value.id === 'account.personal')) {
            return 'Personal info'
        } else if (matches.some(value => value.id === 'account.security')) {
            return 'Security'
        }
    }

    return <>
        <div className='h-14 w-full bg-white border border-gray-200 shadow-sm rounded-b-lg'>
            <div className='grid grid-cols-3 gap-4 h-full'>
                <div className='h-full px-4'>
                    <a className='flex h-full items-center gap-2 font-logo text-3xl' href='/'>
                        <img className='w-8 h-8' src={logo} alt=''/>
                        <span>ID</span>
                    </a>
                </div>
                <div className='flex items-center col-span-2 h-full pl-4'>
                    <span className='text-xl font-semibold'>{getHeader()}</span>
                </div>
            </div>

        </div>
    </>
}

export default AccountHeader