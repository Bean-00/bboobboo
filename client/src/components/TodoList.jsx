import {useEffect, useState} from "react";
import {addTodoAction, getTodoListAction} from "../service/TodoService.js";
import TodoCard from "./TodoCard.jsx";
import LoadPannel from "./LoadPannel.jsx";
import TodoAddModal from "./TodoAddModal.jsx";

export default function TodoList({status}) {
    const [isLoading, setIsLoading] = useState(false);
    const [todoList, setTodoList] = useState([]);
    const [isOpenAddModal, setIsOpenAddModal] = useState();

    const getTodoList = async (status) => {
        setIsLoading(true);

        const {isError, data} = await getTodoListAction(status);
        setIsLoading(false);

        if (isError) {
            alert(`Error: ${data.errorMessage}`);
            return;
        }
        setTodoList(data);
    }

    const addTodo = async (todo) => {
        const {isError, data} = await addTodoAction(todo)
        if (isError) {
            alert(data.errorMessage)
            return;
        }
        setTodoList([...todoList, data])
        console.log("data: ", data, "  :  ", isError)
        setIsOpenAddModal(false)
    }

    useEffect(() => {
        getTodoList(status)
    }, [status]);
    return (
        <>
            <LoadPannel isActive={isLoading}/>
            <div className="flex justify-end pb-3">
                <button type="button"
                        className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 me-2 mb-2 dark:bg-blue-600 dark:hover:bg-blue-700 focus:outline-none dark:focus:ring-blue-800"
                        onClick={() => setIsOpenAddModal(true)}>
                    Add
                </button>
            </div>

            {!isLoading &&
                todoList.map(todo => {
                    return (
                        <div key={todo.id} className={"flex justify-center"}>
                            <TodoCard todo={todo} className={"mt-5 cursor-pointer"}/>
                        </div>
                    )
                })
            }

            <TodoAddModal openModal={isOpenAddModal} onClose={() => {
                setIsOpenAddModal(false)
            }} onAdd={addTodo}/>
        </>
    )
}

