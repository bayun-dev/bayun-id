const usernameRegexp = new RegExp("^(?!.*_{2}.*)[a-zA-Z\\d_]{1,30}$")

export const validateUsername = (username: string): string | undefined => {
    if (username.length === 0) {
        return 'enter a username'
    }

    if (!usernameRegexp.test(username)) {
        return "invalid username"
    }

    return undefined
}

export const validatePassword = (password: string): string | undefined => {
    if (password.length === 0) {
        return 'enter a password'
    }

    return undefined;
}