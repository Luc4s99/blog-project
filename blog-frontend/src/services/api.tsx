import axios from 'axios'
import { getToken } from './authService';
import { toast } from 'react-toastify';

const api = axios.create({
    
    baseURL: 'http://localhost:8080/api/v1',
    withCredentials: true
});

//Interceptor para adicionar o token de autenticação em cada request
api.interceptors.request.use(

    (config) => {

        const token = getToken();

        if(token) {

            config.headers.Authorization = `Bearer ${token}`;
        }

        return config;
    }
);

//Interceptor para verificar se o token ainda é válido após cada resposta do backend
api.interceptors.response.use(

    (response) => response,

    async (error) => {

        const originalRequest = error.config;

        //Se a resposta do servidor for forbidden, significa que o token do usuário não é mais válido
        if(error.response.status === 403 && !originalRequest._retry) {

            originalRequest._retry = true;

            const localStorageUser = localStorage.getItem('user');
            let user = {};

            if(localStorageUser) {

                user = JSON.parse(localStorageUser);
            }

            //Tenta utilizar o refresh token para conseguir um novo acces token
            const refreshResponse = await api.post('/auth/refresh', 
                {
                    user: user
                }
            );

            if(refreshResponse) {

                if(refreshResponse.status === 200) {

                    const newAccessToken = refreshResponse.data.accessToken;
                    localStorage.setItem('token', newAccessToken);

                    return api(originalRequest);
                }
                
            }else {

                //Desloga o usuário
                localStorage.clear();

                toast.error('Sessão expirada, realize o login novamente!', {
                    
                    onClose: () => {

                        window.location.href = '/login';
                    }
                });

                return Promise.reject(error);
            }
        }
    }
);

export default api;