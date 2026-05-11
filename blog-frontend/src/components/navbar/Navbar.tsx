import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom"

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min';
import type { User } from "../../models/User";

export default function Navbar() {

    const [searchCriteria, setSearchCriteria] = useState('');
    const [loggedUser, setLoggedUser] = useState<User>();
    const navigate = useNavigate();

    useEffect(() => {

        const getLoggedUser = () => {

            const localStorageUser = localStorage.getItem('user');

            if(localStorageUser) {

                setLoggedUser(JSON.parse(localStorageUser));
            }
        }

        getLoggedUser();
    }, []);

    const logout = () => {

        localStorage.clear();
        navigate('/');
    }

    return (
    <>
        <nav className="navbar navbar-expand-lg bg-body-tertiary blog-navbar">
            <div className="container-fluid">

                <Link className="navbar-brand" to="/home">Blog</Link>

                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarSupportedContent">

                        {
                            loggedUser ?

                            <>
                                <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                                    <li className="nav-item">
                                        <Link className="nav-link" to="/home">Início</Link>
                                    </li>
                                    
                                    <li className="nav-item">
                                        <Link className="nav-link" to="/novo">Novo</Link>
                                    </li>

                                    <li className="nav-item dropdown">
                                        <a className="nav-link dropdown-toggle" role="button" data-bs-toggle="dropdown" aria-expanded="false">
                                            {loggedUser?.login}
                                        </a>
                                        <ul className="dropdown-menu">
                                            <li><a className="dropdown-item" onClick={logout}>Sair</a></li>
                                        </ul>
                                    </li>
                                </ul>

                                <form className="d-flex" role="search">
                                    <input className="form-control me-2" type="search" placeholder="Search" aria-label="Search"
                                        onChange={(e) => setSearchCriteria(e.target.value)}/>
                                    <Link to={`/posts/search?title=${searchCriteria}`} className="btn btn-primary me-2">Pesquisar</Link>
                                </form>
                            </>
                            
                            :

                            <>
                                <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                                    <li className="nav-item">
                                        <Link to={"/register"} className="btn btn-primary me-2">Registre-se</Link>
                                    </li>

                                    <li className="nav-item">
                                        <Link to={"/login"} className="btn btn-secondary me-2">Logar</Link>
                                    </li>
                                </ul>
                            </>
                        }
                </div>
            </div>
        </nav>
    </>

    );
}