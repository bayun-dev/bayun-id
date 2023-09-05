import {createRoot} from "react-dom/client";
import LoginPage from "./pages/LoginPage";
import {ErrorPageProvider} from "./hooks/useErrorPage";

const LoginPageContextLoader = () => {
    return <>
        <ErrorPageProvider>
            <LoginPage/>
        </ErrorPageProvider>
    </>
}

const main = document.getElementById("p-login")
if (main) {
    createRoot(main).render(<LoginPageContextLoader/>)
}