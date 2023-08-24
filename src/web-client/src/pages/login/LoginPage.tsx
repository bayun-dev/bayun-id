import {Dispatch, SetStateAction, useEffect, useState} from "react";
import {Outlet} from "react-router-dom";
import logo from "../../assets/logo_64x64.png"
import {useLocation} from "react-router-dom";
import {useNavigate} from "react-router";
import Link from "../../component/Link";

export type LoginOutletContextType = {
    username: string,
    setUsername: Dispatch<SetStateAction<string>>
}

const LoginPage = () => {

    const location = useLocation()
    const navigate = useNavigate()

    const [username, setUsername] = useState<string>('')

    const outletContext = { username, setUsername }

    useEffect(() => {
        if (username.trim().length === 0 && location.pathname !== '/login') {
            navigate('/login')
        }
    }, [username])

    return <>
        <div className='flex flex-col justify-center sm:items-center min-h-full bg-gray-50 gap-6'>
            <a className='flex items-center gap-2 font-logo text-3xl' href='/'>
                <img className='w-8 h-8' src={logo} alt=''/>
                <span>bayun</span>
            </a>
            <div className='w-full sm:w-fit bg-white sm:rounded-lg sm:shadow'>
                {/*sm:border sm:border-gray-200*/}
                <div className='w-96 p-8 mx-auto'>
                    <Outlet context={outletContext}/>
                </div>
            </div>
            <div className='text-center'>
                <span className='text-gray-400 text-base'>Don't have Bayun ID? <Link href='/signup'>Create ID</Link></span>
            </div>
        </div>
    </>
}

export default LoginPage