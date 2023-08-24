import BaseRequest, {RequestMethod} from "./BaseRequest";
import PostSignupResponse from "../responses/PostSignupResponse";

export type PostSignupRequestData = {
    username: string
    firstName: string
    lastName: string
    dateOfBirth: string
    gender: string
    email?: string | undefined
    password: string
}

class PostSignupRequest extends BaseRequest<PostSignupResponse, PostSignupRequestData> {

    constructor(data: PostSignupRequestData) {
        super(RequestMethod.POST, '/api/signup', data, {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        });
    }

}

export default PostSignupRequest