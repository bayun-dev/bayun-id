export type IException = {
    code: string
}

class Exception implements IException {
    readonly code: string

    constructor(code: string) {
        this.code = code;
    }
}

export default Exception