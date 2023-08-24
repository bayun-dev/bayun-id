import BaseRequest, {RequestMethod} from "./BaseRequest";
import PostSignupAvailabilityResponse from "../responses/PostSignupAvailabilityResponse";

export type PostSignupAvailabilityRequestData = {
    username: string
}

class PostSignupAvailabilityRequest extends BaseRequest<PostSignupAvailabilityResponse, PostSignupAvailabilityRequestData> {

    constructor(data: PostSignupAvailabilityRequestData) {
        super(RequestMethod.POST, '/api/signup/availability', data, {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        });
    }

}

export default PostSignupAvailabilityRequest