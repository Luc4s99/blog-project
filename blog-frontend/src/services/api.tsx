import axios from 'axios'
import { getToken } from './authService';

const api = axios.create({
    
    baseURL: 'http://localhost:8080/api/v1'
})

//Interceptor
api.interceptors.request.use(

    (config) => {

        const token = getToken();

        if(token) {

            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    }
);

export default api;