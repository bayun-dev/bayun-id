import {createRoot} from "react-dom/client";
import LoginPage from "./pages/login/LoginPage";
import {createBrowserRouter, Outlet, RouterProvider} from "react-router-dom";
import LoginStartFragment from "./pages/login/LoginStartFragment";
import {LoadingProvider} from "./hooks/use-loading";
import {LoginErrorElement, LoginErrorProvider} from "./pages/login/LoginError";
import LoginContinueFragment from "./pages/login/LoginContinueFragment";

const LoginPageContextLoader = () => {
    return <>
        <LoadingProvider>
            <LoginErrorProvider>
                <Outlet/>
            </LoginErrorProvider>
        </LoadingProvider>
    </>
}

const loginPageRouter = createBrowserRouter([
    {
        id: 'login.root',
        path: '/',
        element: <LoginPageContextLoader/>,
        errorElement: <LoginErrorElement/>,
        children: [{
            id: 'login',
            path: '/login',
            element: <LoginPage/>,
            children: [
                {
                    id: 'login.start',
                    path: '/login',
                    element: <LoginStartFragment/>
                },
                {
                    id: 'login.continue',
                    path: '/login/continue',
                    element: <LoginContinueFragment/>
                }
            ]
        }]
    }
])

const main = document.getElementById("p-login")
if (main) {
    createRoot(main).render(<RouterProvider router={loginPageRouter}/>)
}