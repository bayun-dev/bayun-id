import BaseRequest, {getCsrf, RequestMethod} from "./BaseRequest";

export type DeleteAccountsByIdAvatarRequestData = {

}

class DeleteAccountsByIdAvatarRequest extends BaseRequest<any, DeleteAccountsByIdAvatarRequestData> {

    constructor(id: string) {
        super(RequestMethod.DELETE, '/api/accounts/'+id+'/avatar', undefined, {
            headers: {
                'Accept': 'application/json'
            },
            params: {...getCsrf()}
        });
    }

}

export default DeleteAccountsByIdAvatarRequest