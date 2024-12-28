import {TodoStatusTab} from "../components/TodoStatusTab.jsx";
import {useEffect, useState} from "react";
import {fetchGetTodoStatusList} from "../service/TodoService.js";
import TodoList from "../components/TodoList.jsx";
import {TodoStatusContextList} from "../context/TodoContext.jsx";
import {useNavigate} from "react-router-dom";


export default function TodoListPage() {

    const [todoStatusList, setTodoStatusList] = useState([]);
    const [activeTab, setActiveTab] = useState(0);
    const navigate = useNavigate();

    const getTodoStatusList = async () => {
        const {isError, data} = await fetchGetTodoStatusList();
        if (isError) {
            alert(`Error: ${data.errorMessage}`);
            if (data.httpStatusCode === 401)
                navigate('/login');
            return;
        }
        setTodoStatusList(data);
    }

    useEffect(() => {
        getTodoStatusList();
    }, []);

    return (
        <TodoStatusContextList.Provider value={todoStatusList}>
            <h2 className={"text-2xl flex justify-center font-bold mt-4 mb-4"}>TodoList</h2>
            {todoStatusList.length > 0 &&
                <>
                    <TodoStatusTab dataSource={todoStatusList}
                                   activeTab={activeTab}
                                   onActiveTabChange={setActiveTab}
                    />
                    <TodoList status={activeTab}/>
                </>}
        </TodoStatusContextList.Provider>
    )
}