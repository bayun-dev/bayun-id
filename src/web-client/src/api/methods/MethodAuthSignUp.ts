import BaseRequest, {RequestMethod} from "../requests/BaseRequest";
import BaseResponse from "../objects/BaseResponse";

export type Response = BaseResponse

export type Data = {
    username: string
    firstName: string
    lastName: string
    password: string
    email: string
}

class MethodAuthSignUp extends BaseRequest<Response, Data> {

    constructor(data: Data) {
        super(RequestMethod.POST, '/api/methods/auth.signUp', data, {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "Accept": "application/json"
            }
        });
    }
}

export default MethodAuthSignUp