import {useEffect, useState} from "react";
import {fetchGetTodoList} from "../service/TodoService.js";
import TodoCard from "./TodoCard.jsx";
import LoadPannel from "./LoadPannel.jsx";

export default function TodoList({status}) {
    const [isLoading, setIsLoading] = useState(false);
    const [todoList, setTodoList] = useState([]);

    const getTodoList = async (status) => {
        setIsLoading(true);

        const {isError, data} = await fetchGetTodoList(status);
        setIsLoading(false);

        if (isError) {
            alert(`Error: ${data.message}`);
            return;
        }
        setTodoList(data);
    }
    useEffect(() => {
        getTodoList(status)
    }, [status]);
    return (
        <>
            <LoadPannel isActive={isLoading}/>
            {!isLoading &&
                todoList.map(todo => {
                    return (
                        <div key={todo.id} className={"flex justify-center"}>
                            <TodoCard todo={todo} className={"mt-5 cursor-pointer"}/>
                        </div>
                    )
                })
            }

        </>
    )
}

