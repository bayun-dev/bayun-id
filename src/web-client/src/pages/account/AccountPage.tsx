import GetAccountsByIdRequest from "../../api/requests/GetAccountsByIdRequest";
import Account from "../../api/schemas/Account";
import {useLoaderData, useNavigate} from "react-router";
import AccountMenu from "./AccountMenu";
import {LoaderFunctionArgs} from "@remix-run/router/utils";
import {Outlet} from "react-router-dom";
import {useState} from "react";
import AccountHeader from "./AccountHeader";
import GetAccountsByIdResponse from "../../api/responses/GetAccountsByIdResponse";
import {IErrorCode} from "../../api/schemas/IError";
import Exception from "../../utils/Exception";

export const AccountPageLoader = async (args?: LoaderFunctionArgs) => {
    const request = new GetAccountsByIdRequest('me')
    const response = await request.execute()

    if (response.status === 200) {
        const account = response.data

        if (account.deactivation.deactivated) {
            if (account.deactivation.reason === 'BLOCKED') {
                throw new Exception(IErrorCode.ACCOUNT_BLOCKED)
            } else if (account.deactivation.reason === 'DELETED') {
                throw new Exception(IErrorCode.ACCOUNT_DELETED)
            } else {
                throw new Exception(IErrorCode.INTERNAL)
            }
        }

        return account
    } else if (response.status === 403) {
        throw new Exception(IErrorCode.FORBIDDEN)
    } else {
        throw new Exception(IErrorCode.UNKNOWN)
    }
}

export type AccountPageContextType = {
    readonly account: Account,
}

const AccountPage = () => {

    const [account] = useState<Account>(useLoaderData() as Account)

    return <>
        <div className='w-[750px] mx-auto'>
            <div className='mb-4'>
                <AccountHeader account={account}/>
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