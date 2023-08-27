import BaseRequest, {RequestMethod} from "./BaseRequest";
import PostLoginResponse from "../responses/PostLoginResponse";
import GetAccountsByIdResponse from "../responses/GetAccountsByIdResponse";

export type GetAccountsByIdRequestData = {

}

class GetAccountsByIdRequest extends BaseRequest<GetAccountsByIdResponse, GetAccountsByIdRequestData> {

    constructor(id: string) {
        super(RequestMethod.GET, '/api/accounts/'+id, undefined, {
            headers: {
                'Accept': 'application/json'
            }
        });
    }

}

export default GetAccountsByIdRequest