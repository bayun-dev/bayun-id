import BaseRequest, {RequestMethod} from "./BaseRequest";
import PostLoginResponse from "../responses/PostLoginResponse";

export type PatchAccountsByIdRequestData = {
    avatar?: File
    firstName?: string
    lastName?: string
    dateOfBirth?: string
    gender?: string
    email?: string
    password?: string
}

class PatchAccountsByIdRequest extends BaseRequest<PostLoginResponse, {}> {

    constructor(id: string, data: PatchAccountsByIdRequestData) {
        super(RequestMethod.PATCH, '/api/accounts/'+id, data, {
            headers: {
                // 'Content-Type': 'application/x-www-form-urlencoded',
                'Content-Type': 'multipart/form-data',
                'Accept': 'application/json'
            }
        });
    }

}

export default PatchAccountsByIdRequest