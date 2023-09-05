import {createRoot} from "react-dom/client";
import {ErrorPageProvider} from "./hooks/useErrorPage";
import SignupPage from "./pages/SignupPage";

const SignupPageContextLoader = () => {
    return <>
        <ErrorPageProvider>
            <SignupPage/>
        </ErrorPageProvider>
    </>
}

const main = document.getElementById("p-signup")
if (main) {
    createRoot(main).render(<SignupPageContextLoader/>)
}