import BaseResponse from "./BaseResponse";
import IError from "../schemas/IError";

type ErrorResponse = BaseResponse & {
    errors: IError[]
}

export default ErrorResponse