import BaseRequest, {RequestMethod} from "./BaseRequest";
import PostLoginResponse from "../responses/PostLoginResponse";

export type PostLoginRequestData = {
    username: string
    password: string
}

class PostLoginRequest extends BaseRequest<PostLoginResponse, PostLoginRequestData> {

    constructor(data: PostLoginRequestData) {
        super(RequestMethod.POST, '/api/login', data, {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        });
    }

}

export default PostLoginRequest