import {useState} from "react";
import {loginByEmailAndPassword} from "../service/SecurityService.js";
import LoginForm from "../components/LoginForm.jsx";
import {useNavigate} from "react-router-dom";

const LoginPage = () => {
    const navigate = useNavigate();

    const loginSuccess = (user) => {
        navigate("/todo");
    }

    return (
        <div className={"flex justify-center mt-7"}>
            <LoginForm onSuccess={loginSuccess} onFailure={(error) => alert(error.errorMessage)}/>
        </div>

    )
}

export default LoginPage;