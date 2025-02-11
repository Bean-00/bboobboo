import {_fetch} from "./fetch.js";

const serverHost = import.meta.env.VITE_SERVER_HOST;
export const FILE_API_URL = `${serverHost}/api/files`;

export const uploadFilesAction = (formData) => {
    return _fetch(FILE_API_URL,
        {method: "Post", credentials: 'include', body: formData})
}