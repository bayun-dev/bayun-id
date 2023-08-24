export type ApiResponse = {
    ok: boolean
    error?: Error | undefined
    redirectUrl?: string | undefined
    tokenId?: string | undefined
}

export type Error = {
    code: number;
    message: string;
    parameter?: string | undefined;
    subErrors?: Error[] | undefined;
}