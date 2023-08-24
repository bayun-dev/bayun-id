import {createRoot} from "react-dom/client";
import {createBrowserRouter, Outlet, RouterProvider} from "react-router-dom";
import {LoadingProvider} from "./hooks/use-loading";
import SignupPage from "./pages/signup/SignupPage";
import SignupUsernameFragment from "./pages/signup/SignupUsernameFragment";
import SignupPersonFragment from "./pages/signup/SignupPersonFragment";
import SignupContactFragment from "./pages/signup/SignupContactFragment";
import SignupSecretFragment from "./pages/signup/SignupSecretFragment";
import {SignupErrorElement, SignupErrorProvider} from "./pages/signup/SignupError";

const SignupPageContextLoader = () => {
    return <>
        <LoadingProvider>
            <SignupErrorProvider>
                <Outlet/>
            </SignupErrorProvider>
        </LoadingProvider>
    </>
}

const signupPageRouter = createBrowserRouter([
    {
        id: 'signup.root',
        path: '/',
        element: <SignupPageContextLoader/>,
        errorElement: <SignupErrorElement/>,
        children: [{
            id: 'signup',
            path: '/signup',
            element: <SignupPage/>,
            children: [
                {
                    id: 'signup.username',
                    path: '/signup',
                    element: <SignupUsernameFragment/>
                },
                {
                    id: 'signup.person',
                    path: '/signup/person',
                    element: <SignupPersonFragment/>
                },
                {
                    id: 'signup.contact',
                    path: '/signup/contact',
                    element: <SignupContactFragment/>
                },
                {
                    id: 'signup.secret',
                    path: '/signup/secret',
                    element: <SignupSecretFragment/>
                }
            ]
        }]
    }
])

const main = document.getElementById("p-signup")
if (main) {
    createRoot(main).render(<RouterProvider router={signupPageRouter}/>)
}