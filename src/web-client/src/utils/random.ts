export function nextId(prefix?: string, suffix?: string): string {
    return (prefix === undefined ? '' : prefix) + Math.floor(Math.random() * 1000) + (suffix === undefined ? '' : suffix)
}