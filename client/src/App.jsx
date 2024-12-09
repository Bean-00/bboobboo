import React from 'react';
import {TodoNavbar} from "./components/TodoNavbar.jsx";
import {Outlet} from "react-router-dom"; // Flowbite-React에서 제공하는 Button 컴포넌트 import

function App() {
    return (
        <>
            <TodoNavbar/>
            <Outlet/>
        </>
    );
}

export default App;
