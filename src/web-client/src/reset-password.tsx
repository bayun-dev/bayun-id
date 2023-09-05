import {createRoot} from "react-dom/client";
import {ErrorPageProvider} from "./hooks/useErrorPage";
import ResetPasswordPage from "./pages/ResetPasswordPage";

const ResetPasswordPageContextLoader = () => {
    return <>
        <ErrorPageProvider>
            <ResetPasswordPage/>
        </ErrorPageProvider>
    </>
}

const main = document.getElementById("p-reset-password")
if (main) {
    createRoot(main).render(<ResetPasswordPageContextLoader/>)
}