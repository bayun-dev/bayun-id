import {PropsWithChildren, ReactNode} from "react";

const ErrorPageWrapper = (props: { errorElement: ReactNode | undefined }) => {

    return <>
        <div className='fixed inset-0 bg-white grid place-content-center p-4'>
            {props.errorElement}
        </div>
    </>
}

export default ErrorPageWrapper