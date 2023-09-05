const nameRegExp = new RegExp('^(?=.{1,30}$)(?!.*[ \\-.\',]{2})[a-zA-Z]+([a-zA-Z \\-.\',]*[a-zA-Z]+)*$')

function quicklyValidate(name: string): string | undefined {
    if (name.length === 0) {
        return 'Required'
    } else if (!nameRegExp.test(name)) {
        return 'Invalid'
    } else {
        return undefined
    }
}

const toSpecialsRegExp = new RegExp("^(?!.*[ \\-.',]{2}).+$")
const startWithLetterRegExp = new RegExp('^[a-zA-Z]+.*$')
const endWithLetterRegExp = new RegExp('^.*[a-zA-Z]+$')
const alphabetRegExp = new RegExp('^[a-zA-Z \\-.\',]*$')
export function inlineValidate(name: string): string | undefined {
    if (name.length === 0) {
        return undefined
    } else if (30 < name.length) {
        return 'Give a shorter'
    } else if (!alphabetRegExp.test(name)) {
        return 'Use Latin letters and - \' , .'
    } else if (!toSpecialsRegExp.test(name)) {
        return 'Don\'t use two special characters in a row'
    } else if (!startWithLetterRegExp.test(name)) {
        return 'Start with a letter'
    } else if (!endWithLetterRegExp.test(name)) {
        return 'End with a letter'
    } else {
        return undefined
    }
}

export function fullValidate(name: string): string | undefined {
    if (name.length === 0) {
        return 'Required'
    } else {
        return inlineValidate(name)
    }
}