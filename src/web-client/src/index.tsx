import "./index.css"
import {LoadingProvider} from "./hooks/use-loading";
import {createBrowserRouter, Outlet, RouterProvider} from "react-router-dom";
import AccountPage, {AccountPageLoader} from "./pages/account/AccountPage";
import AccountHomePage from "./pages/account/AccountHomePage";
import AccountPersonalPage from "./pages/account/AccountPersonalPage";
import AccountSecurityPage from "./pages/account/AccountSecurityPage";
import {createRoot} from "react-dom/client";
import AccountHeader from "./pages/account/AccountHeader";

const AccountPageContextLoader = () => {
    return <>
        <LoadingProvider>
            <Outlet/>
        </LoadingProvider>
    </>
}

const accountPageRouter = createBrowserRouter([
    {
        id: 'page.root',
        path: '/',
        element: <AccountPageContextLoader/>,
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
                    },                ]
            },
        ],
    },
])

const main = document.getElementById("p-index")
if (main) {
    createRoot(main).render(<RouterProvider router={accountPageRouter}/>)
}