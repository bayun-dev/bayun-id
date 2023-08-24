type RequestWithCsrf<T = any> = T & {
    csrf: string | undefined
}

export default RequestWithCsrf