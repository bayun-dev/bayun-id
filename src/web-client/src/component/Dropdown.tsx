import {HTMLAttributes, PropsWithChildren} from "react";

export type DropdownProps = HTMLAttributes<HTMLDivElement> & PropsWithChildren

const Dropdown = (props: DropdownProps) => {
    return <>
        { !props["aria-hidden"] && <div className={props.className}>
            {props.children}
        </div> }
    </>
}

export default Dropdown