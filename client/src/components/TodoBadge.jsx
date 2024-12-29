import {useContext} from "react";
import {TodoStatusContextList} from "../context/TodoContext.js";

const colorList = ['blue', 'purple', 'green']

export default function TodoStatusBadge({ status }) {
    const statusList = useContext(TodoStatusContextList);

    const getStatusName = (status) => {
        return statusList.filter((item) => item.id === status)[0]?.description;
    }

    return (
        <span
            className={`bg-${colorList[status]}-100 text-${colorList[status]}-800 text-xs font-medium me-2 px-2.5 py-0.5 rounded dark:bg-${colorList[status]}-900 dark:text-${colorList[status]}-300
            border border-${colorList[status]}-500`}>
                    {getStatusName(status)}
                </span>
    )
}