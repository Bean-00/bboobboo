export const fatchGet = async (url) => {

    return _fetch(url, {method: "Get", credentials: 'include'})
}

export const _fetch = async (url, requestInit) => {
    const res = await fetch(url, requestInit);
    const data = await res.json();
    console.log("### ", data);
    return { isError: !res.ok, data: data };
}
