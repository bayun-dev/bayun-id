import {useOutletContext} from "react-router";
import {AccountPageContextType} from "./AccountPage";
import * as React from "react";

const AccountHomePage = () => {

    const context = useOutletContext<AccountPageContextType>()

    return <>
        <div className='flex p-4 gap-x-4 gap-y-8 border border-gray-200 shadow-sm rounded-lg'>
            <div className='h-16 w-16 rounded-full overflow-hidden'>
                <img className='w-full h-full' src={`/avatar/${context.account.avatarId}/large`} alt=''/>
            </div>
            <div className='flex flex-col justify-center'>
                <div>
                    <span className='font-semibold'>{context.account.person?.firstName} {context.account.person?.lastName}</span>
                </div>
                <div className='-translate-y-1'>
                    <span className='text-gray-500'>@{context.account.username}</span>
                </div>
            </div>
        </div>
    </>
}

export default AccountHomePage