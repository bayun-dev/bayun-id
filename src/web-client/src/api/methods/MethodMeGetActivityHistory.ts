import BaseRequest, {RequestMethod} from "../requests/BaseRequest";
import BaseResponse from "../objects/BaseResponse";
import Account from "../objects/Account";
import Session from "../objects/Session";

export type Response = BaseResponse & {
    history: Session[]
}

class MethodMeGetActivityHistory extends BaseRequest<Response, undefined> {

    constructor() {
        super(RequestMethod.GET, '/api/methods/me.getActivityHistory', undefined, {
            headers: {
                "Accept": "application/json"
            }
        });
    }
}

export default MethodMeGetActivityHistory