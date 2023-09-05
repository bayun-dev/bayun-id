import {createRoot} from "react-dom/client";
import useErrorPage, {ErrorPageProvider} from "./hooks/useErrorPage";
import {createBrowserRouter, Outlet, RouterProvider} from "react-router-dom";
import MePage from "./pages/me/MePage";
import MePageLoader from "./pages/me/MePageLoader";
import {useEffect, useState} from "react";
import MethodMeGet from "./api/methods/MethodMeGet";
import Account from "./api/objects/Account";
import ErrorBody, {ErrorType} from "./api/objects/ErrorBody";
import {AxiosError} from "axios";
import {SvgArrowRightOnRectangle, SvgCog6Tooth} from "./component/Svg";
import MethodAuthSignOut from "./api/methods/MethodAuthSignOut";

const WidgetContextLoader = () => {

    return <>
        <ErrorPageProvider>
            <WidgetWrapper/>
        </ErrorPageProvider>
    </>
}

const WidgetWrapper = () => {

    const errorPage = useErrorPage()

    const [me, setMe] = useState<Account>()

    useEffect(() => {
        const method = new MethodMeGet()
        method.execute().then(r => {
            if (r.data.ok) {
                setMe(r.data)
            } else {
                errorPage.show.internalError()
            }
        }).catch((e: AxiosError<ErrorBody>) => {
            if (!e.response || !e.response.data.type) {
                errorPage.show.internalError()
            }

            errorPage.show.internalError()
        })
    }, [])

    return <>
        { me ? <>
            <Widget me={me}/>
        </> : <></>}
    </>
}

const Widget = (props: {me: Account}) => {

    async function onSignOut() {
        const method = new MethodAuthSignOut()
        const r = await method.execute()
        if (r.data.ok) {
            window.top?.location.reload()
        }
    }

    async function toAccountManagement() {
        window.top?.location.replace('/')
    }

    return <>
        <div className='p-4 flex flex-col items-center w-full h-full shadow border border-gray-200 rounded-lg'>
            <div className='mb-4 mt-2'>
                <img className='w-16 h-16 rounded-full ring-2 ring-black ring-offset-2' src={`/avatar/${props.me.avatarId == null ? 'default' : props.me.avatarId}`} alt=''/>
            </div>
            <div className=''>
                <span className='font-medium text-lg'>{props.me.firstName} {props.me.lastName}</span>
            </div>
            <div>
                <span className='text-gray-500'>@{props.me.username}</span>
            </div>
            <button onClick={toAccountManagement} className='flex items-center gap-4 px-4 py-2 mt-4 rounded-lg hover:bg-gray-100 w-full'>
                <div>
                    <SvgCog6Tooth className='w-6 h-6'/>
                </div>
                <div>
                    <span>Account management</span>
                </div>
            </button>
            <button onClick={onSignOut} className='flex items-center gap-4 px-4 py-2 mt-2 rounded-lg hover:bg-gray-100 w-full'>
                <div>
                    <SvgArrowRightOnRectangle className='w-6 h-6'/>
                </div>
                <div>
                    <span>Sign out</span>
                </div>
            </button>
            <div>

            </div>
        </div>
    </>
}

const main = document.getElementById("p-widget")
if (main) {
    createRoot(main).render(<WidgetContextLoader/>)
}