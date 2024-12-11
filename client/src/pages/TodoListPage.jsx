import {TodoStatusTab} from "../components/TodoStatusTab.jsx";
import {useEffect, useState} from "react";
import {fetchGetTodoStatusList} from "../service/TodoService.js";
import TodoList from "../components/TodoList.jsx";
import {TodoStatusContextList} from "../context/TodoContext.jsx";


export default function TodoListPage() {

    const [todoStatusList, setTodoStatusList] = useState([]);
    const [activeTab, setActiveTab] = useState(0);

    const getTodoStatusList = async () => {
        const {isError, data} = await fetchGetTodoStatusList();
        if (isError) {
            alert(`Error: ${data.message}`);
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