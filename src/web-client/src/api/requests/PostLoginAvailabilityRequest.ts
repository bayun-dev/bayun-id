import BaseRequest, {RequestMethod} from "./BaseRequest";
import PostLoginAvailabilityResponse from "../responses/PostLoginAvailabilityResponse";
import {AxiosResponse, AxiosResponseHeaders, InternalAxiosRequestConfig, RawAxiosResponseHeaders} from "axios";

export type PostLoginAvailabilityRequestData = {
    username: string
}

class PostLoginAvailabilityRequest extends BaseRequest<PostLoginAvailabilityResponse, PostLoginAvailabilityRequestData> {

    constructor(data: PostLoginAvailabilityRequestData) {
        super(RequestMethod.POST, '/api/login/availability', data, {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "Accept": 'application/json'
            }
        });
    }

}

export default PostLoginAvailabilityRequest