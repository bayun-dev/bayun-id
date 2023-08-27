import {ReactNode} from "react";
import {SvgArrowPath, SvgArrowRightOnRectangle, SvgEnvelope, SvgKey, SvgTrash} from "../../component/svg";
import {useOutletContext} from "react-router";
import {AccountPageContextType} from "./AccountPage";
import moment from "moment";

const AccountSecurityPage = () => {

    const context = useOutletContext<AccountPageContextType>()

    const passwordHint = 'Last updated ' + moment(context.account.secret?.lastModifiedDate).fromNow()

    return <>
        <div>
            <div className='p-4 rounded-lg border border-gray-200 shadow-sm'>
                <div className='mb-4'>
                    <span className='font-semibold'>Account info</span>
                </div>
                <div>
                    <Item icon={<SvgEnvelope className='w-8 h-8'/>} title='Email' hint={getHiddenEmail(context.account.contact?.email)}/>
                    <Item icon={<SvgKey className='w-8 h-8'/>} title='Password' hint={passwordHint}/>
                </div>
            </div>
            <div className='p-4 rounded-lg border border-gray-200 shadow-sm mt-4'>
                <div className='mb-4'>
                    <span className='font-semibold'>Your sessions</span>
                </div>
                <div>
                    <Item icon={<SvgArrowPath className='w-8 h-8'/>} title='Activity history' hint='Last login date:'/>
                    <Item icon={<SvgArrowRightOnRectangle className='w-8 h-8 text-red-600'/>} title={<span className='text-red-600'>End other sessions</span>} hint='Click if you suspect someone other than you may have logged into your account'/>
                </div>
            </div>
            <div className='p-4 rounded-lg border border-gray-200 shadow-sm mt-4'>
                <div className='mb-4'>
                    <span className='font-semibold'>Actions</span>
                </div>
                <div>
                    <Item icon={<SvgTrash className='w-8 h-8'/>} title='Delete account' hint='You can delete your Bayun ID'/>
                </div>
            </div>
        </div>
    </>
}

type ItemProps = {
    icon?: ReactNode | undefined,
    title?: string | ReactNode | undefined,
    hint?: string | ReactNode | undefined
}
const Item = (props: ItemProps) => {
    return <>
        <a href='#' className='flex items-center gap-4 px-4 py-2 rounded-lg hover:bg-gray-100'>
            <div>
                {props.icon}
            </div>
            <div>
                <div>
                    {props.title}
                </div>
                <div className='text-sm text-gray-500'>
                    {props.hint}
                </div>
            </div>
        </a>
    </>
}

function getHiddenEmail(email: string | undefined) {
    if (!email) {
        return undefined
    }

    const split = email.split('@')
    const local = split[0]
    const domain = split[1]
    let hiddenLocal = local[0]
    if (local.length > 2) {
        hiddenLocal += local[1]
    }
    hiddenLocal += '***'
    return hiddenLocal + '@' + domain
}

export default AccountSecurityPage