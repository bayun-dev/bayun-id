const InternalErrorPage = () => {
    return <>
        <div className='h-full flex justify-center items-center bg-white '>
            <div className='w-[500px] text-center'>
                <div>
                    <span className='text-3xl font-normal'>OOPS</span>
                </div>
                <div className='my-8'>
                    <span className='text-9xl text-red-500'>500</span>
                </div>
                <div className='text-xl text-gray-500'>Looks like, somethings went wrong.</div>
            </div>
        </div>
    </>
}

export default InternalErrorPage