class ResponseStatus {
    readonly status: number
    constructor(status: number) {
        this.status = status;
    }
    isOk(): boolean {
        return this.status === 200
    }

    isBadRequest(): boolean {
        return this.status === 400
    }

    isClientError(): boolean {
        return 400 <= this.status && this.status <= 500
    }

    isServerError(): boolean {
        return 500 <= this.status && this.status <= 600
    }
}

export default ResponseStatus
