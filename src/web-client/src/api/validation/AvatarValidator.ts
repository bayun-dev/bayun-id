const allowedTypes = ['image/png', 'image/jpg', 'image/jpeg']

export function inlineValidate(file: File): string | undefined {
    if (!allowedTypes.includes(file.type)) {
        return 'Only .png and .jpg are supported'
    } else if (2*1024*1024 < file.size) {
        return 'Photos larger than 2MB are not supported'
    } else {
        return undefined
    }
}