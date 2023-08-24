import {Dispatch, SetStateAction, useEffect, useState} from "react";
import {useLocation, useNavigate} from "react-router";
import logo from "../../assets/logo_64x64.png";
import Link from "../../component/Link";
import {Outlet} from "react-router-dom";

export type SignupOutletContextType = {
    username: string,
    setUsername: Dispatch<SetStateAction<string>>

    firstName: string,
    setFirstName: Dispatch<SetStateAction<string>>

    lastName: string,
    setLastName: Dispatch<SetStateAction<string>>

    dateOfBirth: Date,
    setDateOfBirth: Dispatch<SetStateAction<Date>>

    gender: string
    setGender: Dispatch<SetStateAction<string>>

    email: string | undefined,
    setEmail: Dispatch<SetStateAction<string | undefined>>

    getNextUrl: () => string
}

const nextUrlMap: Record<string, string> = {
    '/signup': '/signup/person',
    '/signup/person': '/signup/contact',
    '/signup/contact': '/signup/secret',
    '/signup/secret': '/login',
}

const SignupPage = () => {

    const navigate = useNavigate()
    const location = useLocation()

    const getNextUrl = () => {
        return nextUrlMap[location.pathname]
    }

    const [username, setUsername] = useState<string>('')
    const [firstName, setFirstName] = useState<string>('')
    const [lastName, setLastName] = useState<string>('')
    const [dateOfBirth, setDateOfBirth] = useState<Date>(new Date())
    const [gender, setGender] = useState<string>('male')
    const [email, setEmail] = useState<string | undefined>(undefined)

    const outletContext: SignupOutletContextType = {
        username: username,
        setUsername: setUsername,
        firstName: firstName,
        setFirstName: setFirstName,
        lastName: lastName,
        setLastName: setLastName,
        dateOfBirth: dateOfBirth,
        setDateOfBirth: setDateOfBirth,
        gender: gender,
        setGender: setGender,
        email: email,
        setEmail: setEmail,
        getNextUrl: getNextUrl
    }

    useEffect(() => {
        if (username.trim().length === 0 && location.pathname !== '/signup') {
            navigate('/signup')
        }
    }, [username])

    return <>
        <div className='flex flex-col justify-center sm:items-center min-h-full bg-gray-50 gap-6'>
            <a className='flex items-center gap-2 font-logo text-3xl' href='/'>
                <img className='w-8 h-8' src={logo} alt=''/>
                <span>bayun</span>
            </a>
            <div className='w-full sm:w-fit bg-white sm:rounded-lg sm:shadow'>
                <div className='w-96 p-8 mx-auto'>
                    <Outlet context={outletContext}/>
                </div>
            </div>
            <div className='text-center'>
                <span className='text-gray-400 text-base'>Already have Bayun ID? <Link href='/login'>Log In</Link></span>
            </div>
        </div>
    </>
}

export default SignupPage