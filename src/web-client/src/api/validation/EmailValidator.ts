import {inlineValidate} from "./UsernameValidator";

const emailRegExp = new RegExp('^(?=[\\S@]{3,320}$)\\S+@\\S+$')

export function quicklyValidate(email: string): string | undefined {
    if (email.length === 0) {
        return 'Required'
    } else if (!emailRegExp.test(email)) {
        return 'Invalid'
    }

    return undefined;
}