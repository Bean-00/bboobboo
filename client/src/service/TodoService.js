import {fatchGet} from "./fetch.js";

const serverHost = import.meta.env.VITE_SERVER_HOST;
const TODO_API_URL = `${serverHost}/api/todo`;

export const fetchGetTodoStatusList = () => {
    return fatchGet(`${TODO_API_URL}/status`);
}