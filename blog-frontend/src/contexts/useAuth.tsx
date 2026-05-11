import { useNavigate } from "react-router-dom";
import type { User } from "../models/User";
import { createContext, useEffect, useState } from "react";
import { loginAPI, registerAPI } from "../services/authService";
import React from "react";

type UserContextType = {

  user: User | null;
  token: string | null;
  registerUser: (email: string, username: string, password: string) => void;
  loginUser: (username: string, password: string) => void;
  logout: () => void;
  isLoggedIn: () => boolean;
};

type Props = { children: React.ReactNode };

const UserContext = createContext<UserContextType>({} as UserContextType);

export const UserProvider = ({ children }: Props) => {

    const navigate = useNavigate();
    const [token, setToken] = useState<string | null>(null);
    const [user, setUser] = useState<User | null>(null);
    const [isReady, setIsReady] = useState(false);

    useEffect(() => {

        const loadUser = () => {

            const user = localStorage.getItem("user");
            const token = localStorage.getItem("token");

            if (user && token) {

                setUser(JSON.parse(user));
                setToken(token);
            }
            
            setIsReady(true);
        }

        loadUser();
        
    }, []);

    const registerUser = async (email: string, username: string, password: string) => {

        await registerAPI(email, username, password).then((res) => {
            
            if (res) {

                localStorage.setItem("token", res?.data.token);
                localStorage.setItem("token", res?.data.refreshToken);

                const userObj = {

                    id: res?.data.user.id,
                    login: res?.data.user.username,
                    email: res?.data.user.email
                };

                localStorage.setItem("user", JSON.stringify(userObj));

                setToken(res.data.token);
                setUser(userObj);
                
                navigate("/home");
            }
        })
    };

    const loginUser = async (username: string, password: string) => {

        await loginAPI(username, password).then((res) => {

            if (res) {

                localStorage.setItem("token", res?.data.token);
                localStorage.setItem("refreshToken", res?.data.refreshToken);

                const userObj: User = {

                    id: res?.data.user.id,
                    login: res?.data.user.username,
                    email: res?.data.user.email
                };

                localStorage.setItem("user", JSON.stringify(userObj));

                setToken(res.data.token!);
                setUser(userObj!);
                
                
                navigate("/home");
            }
        });
    };

    const isLoggedIn = () => {

        return !!user;
    };

    const logout = () => {

        localStorage.removeItem("token");
        localStorage.removeItem("user");

        setUser(null);
        setToken("");

        navigate("/login");
    };

    return (

        <UserContext.Provider
        value={{ loginUser, user, token, logout, isLoggedIn, registerUser }}>

        {isReady ? children : null}

        </UserContext.Provider>
    );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => React.useContext(UserContext);