import BaseResponse from "./BaseResponse";
import IError from "../schemas/IError";
import Account from "../schemas/Account";

type GetAccountsByIdResponse = BaseResponse & Account

export default GetAccountsByIdResponse