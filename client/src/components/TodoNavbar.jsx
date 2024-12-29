import {Avatar, Dropdown, Navbar} from "flowbite-react";
import logo from "../assets/react.svg"
import {Link, Router, useNavigate, useNavigation} from "react-router-dom";
import {logoutAction} from "../service/SecurityService.js";
import {useContext} from "react";
import {UserContext} from "../context/UserContext.js";

const TodoNavLink = ({to, text}) => {
    return (
        <Navbar.Link as={'div'}>
            <Link to={to}>
                {text}
            </Link>
        </Navbar.Link>
    )
}

export function TodoNavbar() {

    const {loginUser, dispatch} = useContext(UserContext);

    const navigate = useNavigate();


    const logout = async () => {
        const {isError, data} = await logoutAction();
        if (isError) {
            alert(data.errorMessage);
            return;
        }
        alert('성공적인 로그아웃');
        dispatch({type: "setUser", payload: null});
        navigate('/login')
    }

    // console.log(loginUser);

    return (
        <Navbar fluid rounded>
            <Navbar.Brand href={"/"}>
                <img src={logo} className="mr-3 h-6 sm:h-9" alt="React Logo"/>
                <span className="self-center whitespace-nowrap text-xl font-semibold dark:text-white">TO DO</span>
            </Navbar.Brand>
            <div className="flex md:order-2">
                <Dropdown
                    placement={'bottom-end'}
                    arrowIcon={false}
                    inline
                    label={
                        <Avatar alt="User settings" img="https://flowbite.com/docs/images/people/profile-picture-5.jpg"
                                rounded/>
                    }
                >
                    {loginUser?.id?
                        <>
                            <Dropdown.Header>
                                <span className="block text-sm">{loginUser.name}</span>
                            </Dropdown.Header>
                            <Dropdown.Item onClick={logoutAction}>Sign out</Dropdown.Item>
                        </> :
                        <Dropdown.Header>
                            <Link to={'/login'}>
                                <span className="block text-sm">Login</span>
                            </Link>
                        </Dropdown.Header>
                    }
                </Dropdown>
                <Navbar.Toggle/>
            </div>
            <Navbar.Collapse>
                <TodoNavLink to={'/'} text={'Home'}/>
                <TodoNavLink to={'/todo'} text={'List'}/>
                <TodoNavLink to={'/my-page'} text={'MyPage'}/>
            </Navbar.Collapse>
        </Navbar>
    );
}
