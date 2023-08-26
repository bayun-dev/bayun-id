import BaseRequest, {getCsrf, RequestMethod} from "./BaseRequest";
import PostLoginResponse from "../responses/PostLoginResponse";

export type DeleteAccountsByIdRequestData = {

}

class DeleteAccountsByIdRequest extends BaseRequest<PostLoginResponse, DeleteAccountsByIdRequestData> {

    constructor(id: string) {
        super(RequestMethod.DELETE, '/api/accounts/'+id, undefined, {
            headers: {
                'Accept': 'application/json'
            },
            params: {...getCsrf()}
        });
    }

}

export default DeleteAccountsByIdRequest