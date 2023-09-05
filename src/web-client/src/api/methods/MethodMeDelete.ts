import BaseRequest, {RequestMethod} from "../requests/BaseRequest";
import BaseResponse from "../objects/BaseResponse";
import Account from "../objects/Account";

export type Data = {
    password: string
}

export type Response = BaseResponse

class MethodMeDelete extends BaseRequest<Response, Data> {

    constructor(data: Data) {
        super(RequestMethod.POST, '/api/methods/me.delete', data, {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "Accept": "application/json"
            }
        });
    }
}

export default MethodMeDelete