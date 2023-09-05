import BaseRequest, {RequestMethod} from "../requests/BaseRequest";
import BaseResponse from "../objects/BaseResponse";
import Account from "../objects/Account";

export type Data = {
    firstName?: string | undefined
    lastName?: string | undefined
    password?: string | undefined
    email?: string | undefined
    avatar?: File | undefined
    dropAvatar?: boolean
}

export type Response = BaseResponse

class MethodMeSave extends BaseRequest<Response, Data> {

    constructor(data: Data) {
        super(RequestMethod.POST, '/api/methods/me.save', data, {
            headers: {
                "Content-Type": "multipart/form-data",
                "Accept": "application/json"
            }
        });
    }
}

export default MethodMeSave