import {_fetch, fetchPost} from "./fetch.js";

const serverHost = import.meta.env.VITE_SERVER_HOST;
const FILE_API_URL = `${serverHost}/api/files`;

export const uploadFilesAction = (formData) => {
    console.log("@@2: ", formData);
    return _fetch(FILE_API_URL,
        {method: "Post", credentials: 'include', body: formData})
}