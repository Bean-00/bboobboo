import {useContext, useState} from "react";
import {loginByEmailAndPasswordAction} from "../service/SecurityService.js";
import LoginForm from "../components/LoginForm.jsx";
import {useNavigate} from "react-router-dom";
import {UserContext} from "../context/UserContext.js";

const LoginPage = () => {

    const {dispatch} = useContext(UserContext);
    const navigate = useNavigate();

    const loginSuccess = (user) => {
        dispatch({type: "setUser", payload: user});
        navigate("/todo");
    }

    return (
        <div className={"flex justify-center mt-7"}>
            <LoginForm onSuccess={loginSuccess} onFailure={(error) => alert(error.errorMessage)}/>
        </div>

    )
}

export default LoginPage;