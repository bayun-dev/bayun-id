import BaseRequest, {RequestMethod} from "../requests/BaseRequest";
import BaseResponse from "../objects/BaseResponse";

export type Response = BaseResponse & {
    redirectUri: string
}

export type Data = {
    username: string
    password: string
}

class MethodAuthSignIn extends BaseRequest<Response, Data> {

    constructor(data: Data) {
        super(RequestMethod.POST, '/api/methods/auth.signIn', data, {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "Accept": "application/json"
            }
        });
    }
}

export default MethodAuthSignIn