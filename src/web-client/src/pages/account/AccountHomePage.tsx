import logo from "../../assets/logo_64x64.png";
import {SvgHome, SvgIdentification, SvgShieldCheck} from "../../component/svg";

const AccountHomePage = () => {
    return <>
        <div>
            <div className='h-14 w-full bg-white mb-4 pl-4 rounded-l-lg'>
                <a className='flex h-full items-center gap-2 font-logo text-3xl' href='/'>
                    <img className='w-8 h-8' src={logo} alt=''/>
                    <span>ID</span>
                </a>
            </div>
            <div className='flex flex-col bg-white shadow rounded-lg py-2'>
            </div>

        </div>
    </>
}

export default AccountHomePage