import axios, {AxiosRequestConfig, AxiosResponse} from "axios";
import BaseResponse from "../responses/BaseResponse";
import RequestWithCsrf from "./RequestWithCsrf";

export enum RequestMethod {
    POST,
    GET
}

abstract class BaseRequest<R = BaseResponse, D = any> {

    private readonly method: RequestMethod
    private readonly url: string
    private readonly data?: D
    private readonly config?: AxiosRequestConfig<D>

    protected constructor(method: RequestMethod, url: string, data?: D, config?: AxiosRequestConfig<D>) {
        this.method = method
        this.url = url
        this.data = data
        this.config = config;
    }

    async execute(): Promise<AxiosResponse<R>> {
        switch (this.method) {
            case RequestMethod.GET:
                return await axios.get<R>(this.url, this.config)
            case RequestMethod.POST:
                console.log(getCsrf())
                return await axios.post<R, AxiosResponse<R>>(this.url, { ...this.data, ...getCsrf()}, this.config)
        }
    }
}

const getCsrf = () => {
    return { _csrf: document.querySelector<HTMLMetaElement>('meta[name="_csrf"]')?.content}
}

export default BaseRequest