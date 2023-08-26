import InputText from "../../component/InputText";
import Select from "../../component/Select";
import InputDate from "../../component/InputDate";
import Button from "../../component/Button";
import Account from "../../api/schemas/Account";
import {useState} from "react";
import moment from "moment";
import {useOutletContext} from "react-router";
import {AccountPageContextType} from "./AccountPage";

const AccountPersonalPage = () => {

    const context = useOutletContext<AccountPageContextType>()

    const [firstName, setFirstName] = useState<string>('')
    const [lastName, setLastName] = useState<string>('')
    const [dateOfBirth, setDateOfBirth] = useState<Date>(moment(context.account.person?.dateOfBirth!, 'DD.MM.yyyy').toDate())
    const [gender, setGender] = useState<string>(context.account.person?.gender!)

    return <>
        <div className='grid grid-cols-2 p-4 gap-x-4 gap-y-8 border border-gray-200 shadow-sm rounded-lg'>
            <InputText label='First Name' value={firstName} defaultValue={context.account.person?.firstName!}/>
            <InputText label='Last Name' value={lastName} defaultValue={context.account.person?.lastName!}/>
            <Select label='Gender' value={gender} onChange={(e) => {}} values={{male: 'Male', female: 'Female'}}/>
            <InputDate label='Date of birth' selected={dateOfBirth} onChange={(date) => setDateOfBirth(date)} dateFormat='dd.MM.yyyy'/>
            <div className='col-span-2'>
                <Button type='dark'>Save</Button>
            </div>
        </div>
    </>
}

export default AccountPersonalPage