import {useOutletContext} from "react-router";
import {AccountPageContextType} from "./AccountPage";

const AccountHomePage = () => {

    const context = useOutletContext<AccountPageContextType>()

    return <>
        <div className='p-4 gap-x-4 gap-y-8 border border-gray-200 shadow-sm rounded-lg'>
            <div>
                <div>
                    <span className='font-semibold'>{context.account.person?.firstName} {context.account.person?.lastName}</span>
                </div>
                <div className=''>
                    <span className='text-gray-500'>@{context.account.username}</span>
                </div>
            </div>
        </div>
    </>
}

export default AccountHomePage