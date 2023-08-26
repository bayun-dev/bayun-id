import BaseRequest, {RequestMethod} from "./BaseRequest";
import PostLoginResponse from "../responses/PostLoginResponse";

export type PatchAccountsByIdRequestData = {
    firstName?: string
    lastName?: string
    dateOfBirth?: string
    gender?: string
    email?: string | undefined
    password?: string
}

class PatchAccountsByIdRequest extends BaseRequest<PostLoginResponse, {}> {

    constructor(id: string, data: PatchAccountsByIdRequestData) {
        super(RequestMethod.PATCH, '/api/accounts/'+id, data, {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'Accept': 'application/json'
            }
        });
    }

}

export default PatchAccountsByIdRequest