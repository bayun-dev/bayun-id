import BaseRequest, {RequestMethod} from "./BaseRequest";
import PostLoginResponse from "../responses/PostLoginResponse";

export type GetAccountsByIdRequestData = {

}

class GetAccountsByIdRequest extends BaseRequest<PostLoginResponse, GetAccountsByIdRequestData> {

    constructor(id: string) {
        super(RequestMethod.GET, '/api/accounts/'+id, undefined, {
            headers: {
                'Accept': 'application/json'
            }
        });
    }

}

export default GetAccountsByIdRequest