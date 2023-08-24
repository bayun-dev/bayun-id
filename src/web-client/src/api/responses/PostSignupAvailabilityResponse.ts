import BaseResponse from "./BaseResponse";

type PostSignupAvailabilityResponse = BaseResponse & {
    available: boolean
}

export default PostSignupAvailabilityResponse