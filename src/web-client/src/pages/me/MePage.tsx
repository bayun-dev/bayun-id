import {useLoaderData} from "react-router";
import Account from "../../api/objects/Account";
import MePageHeader from "./MePageHeader";
import * as React from "react";
import MePersonalSection from "./MePersonalSection";
import MeContactSection from "./MeContactSection";
import MeLoginMethodsSection from "./MeLoginMethodsSection";
import MeDeleteSection from "./MeDeleteSection";
import moment from "moment";

function retrieveRegistrationDate(uuid: string) {
    const raw = uuid.replace('-','').substring(0,12)
    const bigint = parseInt(raw, 16)
    return moment(bigint).fromNow(true)
}

const MePage = () => {

    const me = useLoaderData() as Account

    return <>
        <div className='w-full h-full flex flex-col'>
            <MePageHeader/>
            <main className='w-full sm:w-sm lg:w-md mx-auto bg-white mt-4 place-self-center space-y-2 grow'>
                <MePersonalSection/>
                <MeContactSection/>
                <MeLoginMethodsSection/>
                <MeDeleteSection/>
            </main>
            <footer className='text-center py-2'>
                <span className='text-sm text-gray-500'>You are with us for {retrieveRegistrationDate(me.id)} &copy; Bayun</span>
            </footer>
        </div>
    </>
}

export default MePage