import {Contact, Person} from "./objects";

export type AuthPayload = {
    username: string,
    password: string,
}

export type AuthStartPayload = {
    username: string,
}

export type AuthCommitPayload = {
    password: string,
    tokenId: string
}

export type RegAccountPayload = {
    username: string
}

export type RegPersonPayload = Person & { tokenId: string }
export type RegContactPayload = Contact & { tokenId: string }
export type RegSecurityPayload = {
    password: string
    tokenId: string
}