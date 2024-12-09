import {Avatar, Dropdown, Navbar} from "flowbite-react";
import logo from "../assets/react.svg"
import {Link, Router} from "react-router-dom";

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
                    <Dropdown.Header>
                        <span className="block text-sm">User</span>
                    </Dropdown.Header>
                    <Dropdown.Item>Sign out</Dropdown.Item>
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
