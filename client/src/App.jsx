import React, {useContext, useEffect, useReducer, useState} from 'react';
import {TodoNavbar} from "./components/TodoNavbar.jsx";
import {Outlet} from "react-router-dom";
import {UserContext} from "./context/UserContext.js";
import {getLoginUserAction} from "./service/SecurityService.js";
import UserReducer from "./reducer/UserReducer.js"; // Flowbite-React에서 제공하는 Button 컴포넌트 import

function App() {

    // const [loginUser, setLoginUser] = useState(null);
    const [loginUser, dispatch] = useReducer(UserReducer, null);

    const getLoginUser = async () => {
        const {isError, data} = await getLoginUserAction()
        if (isError) {
            alert(data.errorMessage)
            return
        }

        dispatch({type: "setUser", payload: data});
    }


    useEffect(() => {
        getLoginUser();
    }, [])
    return (
        <UserContext.Provider value={{loginUser, dispatch}}>
            <TodoNavbar/>
            <Outlet/>
        </UserContext.Provider>
    );
}

export default App;
