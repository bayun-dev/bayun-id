const passwordRegExp = new RegExp('^(?=.{8,60}$).*$')

export function quicklyValidate(password: string): string | undefined {
    if (password.length === 0) {
        return 'Required'
    } else if (!passwordRegExp.test(password)) {
        return 'Invalid'
    } else {
        return undefined;
    }
}

export function inlineValidate(password: string): string | undefined {
    if (password.length === 0) {
        return undefined
    } if (60 < password.length) {
        return 'Too long'
    } else {
        return undefined
    }
}

export function fullValidate(password: string): string | undefined {
    if (password.length === 0) {
        return 'Required'
    } else if (password.length < 8) {
        return 'Too short'
    } else {
        return inlineValidate(password)
    }
}