import BaseResponse from "./BaseResponse";

type PostLoginResponse = BaseResponse & {
    redirectUrl: string
}

export default PostLoginResponse