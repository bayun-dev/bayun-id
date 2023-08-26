import GetAccountsByIdRequest from "../../api/requests/GetAccountsByIdRequest";
import Account from "../../api/schemas/Account";
import {useLoaderData} from "react-router";
import AccountMenu from "./AccountMenu";
import {LoaderFunctionArgs} from "@remix-run/router/utils";
import {Outlet} from "react-router-dom";
import {useState} from "react";
import AccountHeader from "./AccountHeader";

export const AccountPageLoader = async (args: LoaderFunctionArgs) => {
    const request = new GetAccountsByIdRequest('me')
    const response = await request.execute()
    if (response.status === 200) {
        return response.data
    } else {
        throw response
    }
}

export type AccountPageContextType = {
    account: Account
}

const AccountPage = () => {

    const [account, setAccount] = useState<Account>(useLoaderData() as Account)

    return <>
        <div className='w-[750px] mx-auto'>
            <div className='mb-4'>
                <AccountHeader/>
            </div>
            <div className='grid grid-cols-3 gap-4'>
                <div className=''>
                    <AccountMenu/>
                </div>
                <div className='col-span-2'>
                    <Outlet context={{account}}/>
                </div>
            </div>
        </div>
    </>
}

export default AccountPage