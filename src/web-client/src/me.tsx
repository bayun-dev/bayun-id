import {createRoot} from "react-dom/client";
import {ErrorPageProvider} from "./hooks/useErrorPage";
import {createBrowserRouter, Outlet, RouterProvider} from "react-router-dom";
import MePage from "./pages/me/MePage";
import MePageLoader from "./pages/me/MePageLoader";
import MeErrorElement from "./pages/me/MeErrorElement";

const mePageRouter = createBrowserRouter([
    {
        id: 'page.root',
        path: '/',
        element: <Outlet/>,
        errorElement: <MeErrorElement/>,
        children: [
            {
                id: 'me',
                path: '/',
                element: <MePage/>,
                loader: MePageLoader,
                children: []
            },
        ],
    }
])

const MePageContextLoader = () => {
    return <>
        <ErrorPageProvider>
            <RouterProvider router={mePageRouter}/>
        </ErrorPageProvider>
    </>
}

const main = document.getElementById("p-me")
if (main) {
    createRoot(main).render(<MePageContextLoader/>)
}