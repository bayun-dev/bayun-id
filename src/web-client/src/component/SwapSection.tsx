import {HTMLAttributes, MouseEventHandler, PropsWithChildren, ReactNode, useState} from "react";
import Section, {SectionProps} from "./Section";

type SwapSectionProps = PropsWithChildren<SectionProps & {
    swapElement: ReactNode
    swap: boolean
} & HTMLAttributes<HTMLDivElement>>

export type SwapElementProps = {
    swap: () => void
}

const SwapSection = (props: SwapSectionProps) => {
    return <>
        <Section title={props.title} description={props.description}
                 className={props.swap ? undefined : 'hover:rounded-lg hover:bg-neutral-100 cursor-pointer'}
                 onClick={props.swap ? undefined : props.onClick}>
            {props.swap ? props.swapElement : props.children}
        </Section>
    </>
}

export default SwapSection