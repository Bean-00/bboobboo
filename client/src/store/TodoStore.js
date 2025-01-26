import {create} from "zustand";

const TodoStore = create((set)=>({
    todoList: [],
    setTodoList: (todoList) => set((state)=> ({ todoList })),
    addTodo: (todo) => set((state)=> ({todoList: [...state.todoList, todo]})),
    removeTodo: (todoId) => set((state) => ({todoList: remove(state.todoList, todoId)})),
    changeTodo: (newTodo) => set((state)=> ({todoList: update(state.todoList, newTodo)}))
}))

const remove = (todoList, id) => {
    const idx = todoList.findIndex(todo => todo.id === id)
    if (idx < 0) {
        return;
    }
    return [...todoList.slice(0, idx), ...todoList.slice(idx + 1)]
}

const update = (todoList, data) => {
    const idx = todoList.findIndex(todo => todo.id === data.id)
    todoList[idx] = data
    return [...todoList]
}

export default TodoStore