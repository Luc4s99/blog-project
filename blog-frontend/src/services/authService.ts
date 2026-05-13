import type { User } from "../models/User";
import api from "./api";

export const TOKEN_KEY = 'token';
export const USER_KEY = 'user';

export const loginAPI = async (username: string, password: string) => {

    const data = await api.post('/auth/login', {

        email: username,
        password: password,
    });

    return data;
};

export const registerAPI = async (email: string, username: string, password: string) => {

    const data = await api.post('auth/register', {

        email: email,
        login: username,
        password: password,
    });

    return data;
};

export const isAuthenticated = () => {

    return localStorage.getItem(TOKEN_KEY) !== null;
}

export const getToken = () => {

    return localStorage.getItem(TOKEN_KEY)
}

export const getUser = (): User | null => {

    const user = localStorage.getItem(USER_KEY);

    if(user) {

        return JSON.parse(user);
    }

    return null;
}

export const saveToken = (token: string) => {

    localStorage.setItem(TOKEN_KEY, token);
}

export const deleteToken = () => {

    localStorage.removeItem(TOKEN_KEY);
}

export const login = async(email: string, password: string) => {

    return await api.post('/auth/login', {email, password});
}

export const logoutAPI = async() => {

    return await api.post('/auth/logout');
}