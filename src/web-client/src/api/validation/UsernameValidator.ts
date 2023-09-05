const usernameRegExp = new RegExp("^(?!.*_{2}.*)[a-zA-Z\\d_]{1,30}$")

export function quicklyValidate(username: string): string | undefined {
    if (username.length === 0) {
        return 'Required'
    } else if (!usernameRegExp.test(username)) {
        return "Invalid"
    } else {
        return undefined
    }
}

const toUnderlinesRexExp = new RegExp("^(?!.*_{2}.*).*$")
const alphabetRegExp = new RegExp('^[a-zA-Z\\d_]+$')
export function inlineValidate(username: string): string | undefined {
    if (username.length === 0) {
        return undefined
    } else if (30 < username.length) {
        return 'Too long'
    } else if (!toUnderlinesRexExp.test(username)) {
        return 'Don\'t use two underlines in a row'
    } else if (!alphabetRegExp.test(username)) {
        return 'Use Latin letters, numbers and underlines'
    } else {
        return undefined
    }
}

export function fullValidate(username: string): string | undefined {
    if (username.length === 0) {
        return 'Required'
    } else {
        return inlineValidate(username)
    }
}