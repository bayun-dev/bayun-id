import {
    AuthCommitPayload,
    AuthPayload,
    AuthStartPayload,
    RegAccountPayload,
    RegContactPayload,
    RegPersonPayload, RegSecurityPayload
} from "./payloads";
import axios, {AxiosResponse} from "axios";
import {ApiResponse} from "./responses";

const getCsrf = () => {
    return { csrf: document.querySelector<HTMLMetaElement>('meta[name="csrf"]')?.content}
}

export const auth = {
    start: async(payload: AuthStartPayload): Promise<ApiResponse> => {
        const data = {...payload, ...getCsrf()}
        const config = {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        }

        return axios.post<ApiResponse, AxiosResponse<ApiResponse>>('/api/auth.start', data, config).then(r => {
                if (r.status === 200) {
                    return r.data
                } else {
                    throw r
                }
            }
        )
    },
    commit: async(payload: AuthCommitPayload): Promise<ApiResponse> => {
        const data = {...payload, ...getCsrf()}
        const config = {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        }

        return axios.post<ApiResponse, AxiosResponse<ApiResponse>>('/api/auth.commit', data, config).then(r => {
                if (r.status === 200) {
                    return r.data
                } else {
                    throw r
                }
            }
        )
    }
}

export const register = {
    account: async (payload: RegAccountPayload): Promise<ApiResponse> => {
        const data = {...payload, ...getCsrf()}
        const config = {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        }

        return axios.post<ApiResponse, AxiosResponse<ApiResponse>>('/api/register.account', data, config).then(r => {
                if (r.status === 200) {
                    return r.data
                } else {
                    throw r
                }
            }
        )
        // return {
        //     ok: true,
        //     tokenId: 'tokenId'
        // }
    },
    person: async (payload: RegPersonPayload): Promise<ApiResponse> => {
        const data = {...payload, ...getCsrf()}
        const config = {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        }

        return axios.post<ApiResponse, AxiosResponse<ApiResponse>>('/api/register.person', data, config).then(r => {
                if (r.status === 200) {
                    return r.data
                } else {
                    throw r
                }
            }
        )
        // return {
        //     ok: true
        // }
    },
    contact: async (payload: RegContactPayload): Promise<ApiResponse> => {
        const data = {...payload, ...getCsrf()}
        const config = {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        }

        // return axios.post<ApiResponse, AxiosResponse<ApiResponse>>('/api/register.person', data, config).then(r => {
        //         if (r.status === 200) {
        //             return r.data
        //         } else {
        //             throw r
        //         }
        //     }
        // )
        return {
            ok: true
        }
    },
    security: async (payload: RegSecurityPayload): Promise<ApiResponse> => {
        const data = {...payload, ...getCsrf()}
        const config = {
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            }
        }

        // return axios.post<ApiResponse, AxiosResponse<ApiResponse>>('/api/register.person', data, config).then(r => {
        //         if (r.status === 200) {
        //             return r.data
        //         } else {
        //             throw r
        //         }
        //     }
        // )
        return {
            ok: true
        }
    }

}