import BaseResponse from "./BaseResponse";

type PostLoginAvailabilityResponse = BaseResponse & {
    available: boolean
    reason?: string
}

export default PostLoginAvailabilityResponse