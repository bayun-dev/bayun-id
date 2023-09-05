import BaseRequest, {RequestMethod} from "../requests/BaseRequest";
import BaseResponse from "../objects/BaseResponse";

export type Response = BaseResponse & {
    email: string
}

export type Data = {
    username: string
}

class MethodAuthResetPassword extends BaseRequest<Response, Data> {

    constructor(data: Data) {
        super(RequestMethod.POST, '/api/methods/auth.resetPassword', data, {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "Accept": "application/json"
            }
        });
    }
}

export default MethodAuthResetPassword