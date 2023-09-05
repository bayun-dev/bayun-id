import {useRouteError} from "react-router";
import {useEffect} from "react";
import {ErrorType} from "../../api/objects/ErrorBody";
import useErrorPage from "../../hooks/useErrorPage";

const MeErrorElement = () => {

    const error = useRouteError()
    const errorPage = useErrorPage()

    useEffect(() => {
        if (error === ErrorType.AUTH_RESTART) {
            window.location.replace('/login')
        }
    }, [error])

    return <>

    </>
}

export default MeErrorElement