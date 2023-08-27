import InputText from "../../component/InputText";
import Button from "../../component/Button";
import * as React from "react";
import {ChangeEvent, ReactNode, useEffect, useState} from "react";
import {useNavigate, useOutletContext} from "react-router";
import {AccountPageContextType} from "./AccountPage";
import {validatePassword} from "../signup/SignupValidation";
import PatchAccountsByIdRequest from "../../api/requests/PatchAccountsByIdRequest";
import IError, {IErrorCode} from "../../api/schemas/IError";
import {SvgKey, SvgTrash} from "../../component/svg";
import Checkbox from "../../component/Checkbox";
import DeleteAccountsByIdRequest from "../../api/requests/DeleteAccountsByIdRequest";

const AccountPasswordChangePage = () => {

    const navigate = useNavigate()

    async function onDelete() {
        const request = new DeleteAccountsByIdRequest('me')
        request.execute().then(r => {
            if (r.status === 200) {
                navigate('/', { replace:true })
            } else {
                throw r
            }
        }, (t) => {
            if (t.response.data.errors) {
                const errors: IError[] = t?.response?.data?.errors
                errors.forEach(error => {
                    if (error.code === IErrorCode.ACCOUNT_NOT_FOUND) {

                    } else {
                        throw error
                    }
                })
            } else {
                throw new Error()
            }
        }).catch(error => {

        })
    }

    function onCancel() {
        navigate('/security')
    }

    return <>
        <div className='flex flex-col items-center p-4 gap-4 border border-gray-200 shadow-sm rounded-lg'>
            <div className='grid'>
                <SvgTrash className='w-12 h-12 place-self-center'/>
            </div>
            <div className='text-center'>
                <span className='font-medium text-xl'>Account deletion</span>
            </div>
            <div className='text-center w-3/4'>
                <span className='text-sm text-gray-500'>Once deleted, the account cannot be recovered.<br/>Are you sure?</span>
            </div>
            <div className='w-3/4 grid grid-cols-2 gap-4'>
                <Button type='dark' onClick={onCancel}>No</Button>
                <Button type='light' onClick={onDelete}>Yes</Button>
            </div>
        </div>
    </>
}

export default AccountPasswordChangePage