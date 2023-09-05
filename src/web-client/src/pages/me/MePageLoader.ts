import MethodMeGet from "../../api/methods/MethodMeGet";
import Account from "../../api/objects/Account";
import {AxiosError} from "axios";
import ErrorBody, {ErrorType} from "../../api/objects/ErrorBody";

const MePageLoader = async (): Promise<Account | void> => { // non void, just typescript

    const method = new MethodMeGet()
    return method.execute().then(r => {
        if (r.data.ok) {
            return r.data as Account
        } else {
            throw ErrorType.INTERNAL_ERROR
        }
    }, (e: AxiosError<ErrorBody>) => {
        if (!e.response || !e.response.data.type) {
            throw ErrorType.INTERNAL_ERROR
        }

        throw e.response.data.type
    })
}

export default MePageLoader