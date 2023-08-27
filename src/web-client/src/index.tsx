import "./index.css"
import {LoadingProvider} from "./hooks/use-loading";
import {createBrowserRouter, Outlet, RouterProvider} from "react-router-dom";
import AccountPage, {AccountPageLoader} from "./pages/account/AccountPage";
import AccountHomePage from "./pages/account/AccountHomePage";
import AccountPersonalPage from "./pages/account/AccountPersonalPage";
import AccountSecurityPage from "./pages/account/AccountSecurityPage";
import {createRoot} from "react-dom/client";
import AccountEmailChangePage from "./pages/account/AccountEmailChangePage";
import AccountPasswordChangePage from "./pages/account/AccountPasswordChangePage";
import AccountDeletePage from "./pages/account/AccountDeletePage";
import ErrorPage, {ErrorPageProvider} from "./pages/error/ErrorPage";

const AccountPageContextLoader = () => {
    return <>
        <ErrorPageProvider>
            <LoadingProvider>
                <Outlet/>
            </LoadingProvider>
        </ErrorPageProvider>
    </>
}

const accountPageRouter = createBrowserRouter([
    {
        id: 'page.root',
        path: '/',
        element: <AccountPageContextLoader/>,
        errorElement: <ErrorPage/>,
        children: [
            {
                id: 'account',
                path: '/',
                element: <AccountPage/>,
                loader: AccountPageLoader,
                children: [
                    {
                        id: 'account.home',
                        path: '/',
                        element: <AccountHomePage/>
                    },
                    {
                        id: 'account.personal',
                        path: '/personal',
                        element: <AccountPersonalPage/>
                    },
                    {
                        id: 'account.security',
                        path: '/security',
                        element: <AccountSecurityPage/>
                    },
                    {
                        id: 'account.email.change',
                        path: '/email-change',
                        element: <AccountEmailChangePage/>
                    },
                    {
                        id: 'account.password.change',
                        path: '/password-change',
                        element: <AccountPasswordChangePage/>
                    },
                    {
                        id: 'account.delete',
                        path: '/delete',
                        element: <AccountDeletePage/>
                    }
                ]
            },
        ],
    },
])

const main = document.getElementById("p-index")
if (main) {
    createRoot(main).render(<RouterProvider router={accountPageRouter}/>)
}