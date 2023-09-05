import {MouseEventHandler, PropsWithChildren} from "react";

export type SectionProps = PropsWithChildren<{
    title: string,
    description?: string | undefined,
    className?: string | undefined,
    onClick?: MouseEventHandler<HTMLElement> | undefined
}>

const Section = (props: SectionProps) => {
    return <section className={['w-full p-4', props.className].join(' ')} onClick={props.onClick}>
        <div>
            <span className='font-medium text-lg'>{props.title}</span>
        </div>
        { props.description && <div>
            <span className='text-sm'>{props.description}</span>
        </div> }
        { props.children && <div className='mt-4 w-full'>{props.children}</div>}
    </section>
}

export default Section