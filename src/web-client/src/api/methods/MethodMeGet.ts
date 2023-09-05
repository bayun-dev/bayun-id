import BaseRequest, {RequestMethod} from "../requests/BaseRequest";
import BaseResponse from "../objects/BaseResponse";
import Account from "../objects/Account";

export type Response = BaseResponse & Account

class MethodMeGet extends BaseRequest<Response, undefined> {

    constructor() {
        super(RequestMethod.GET, '/api/methods/me.get', undefined, {
            headers: {
                "Accept": "application/json"
            }
        });
    }
}

export default MethodMeGet