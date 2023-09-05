import BaseRequest, {RequestMethod} from "../requests/BaseRequest";
import BaseResponse from "../objects/BaseResponse";

export type Response = BaseResponse

export type Data = {}

class MethodAuthSignOut extends BaseRequest<Response, Data> {

    constructor() {
        super(RequestMethod.POST, '/api/methods/auth.signOut', {}, {
            headers: {
                "Accept": "application/json"
            }
        });
    }
}

export default MethodAuthSignOut