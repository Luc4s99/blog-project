import Navbar from '../components/navbar/Navbar'
import Footer from '../components/footer/Footer'
import api from "../services/api"

import { useEffect, useState } from 'react';
import { toast } from 'react-toastify';
import { useNavigate, useParams } from 'react-router-dom';
import type { Post } from '../models/Post';
import { getUser } from '../services/authService';

export default function PostForm() {

    const [title, setTitle] = useState("");
    const [content, setContent] = useState("");
    const navigate = useNavigate();
    const { id } = useParams();

    useEffect(() => {

        const verifyEdit = async () => {

            if(id) {

                const response = await api.get(`/posts/${id}`);
                const postReceived: Post = response.data;

                setTitle(postReceived.title);
                setContent(postReceived.content);
            }
        }

        verifyEdit();
    }, [id]);

    const savePost = async () => {

        const loggedUser = getUser();

        if(id) {

            const editedPost = {id, title, content, author: loggedUser?.id, createdAt: new Date()};

            await api.put("/posts", editedPost);
        }else {

            const newPost = {title, content, author: loggedUser?.id, createdAt: new Date()};

            await api.post("/posts", newPost);
        }
        
        notifySuccess('Post salvo com sucesso!');
        navigate('/home');
    }

    const deletePost = () => {

        api.delete(`/posts?post=${id}`);
        
        notifySuccess('Post excluído com sucesso!');
        navigate('/home');
    }

    const notifySuccess = (message: string) => {
    
        toast.success(message);
    }

    const goBack = () => {

        navigate('/home');
    }

    return (
        <div className='blogBody'>
            <Navbar />

            <div className='container'>

                <button type="button" className="btn btn-secondary mb-2 mt-2" onClick={goBack}>Voltar</button>

                <form>

                    <div className="mb-3 mt-2">
                        <label htmlFor="postTitle" className="form-label">Título</label>
                        <input id="postTitle" value={title} className="form-control" type="text" placeholder="Título do post" aria-label="default input example"
                            onChange={(e) => setTitle(e.target.value)}></input>
                    </div>

                    <div className="mb-3">
                        <label htmlFor="contentText" className="form-label">Conteúdo</label>
                        <textarea className="form-control" value={content} id="contentText" rows={15} 
                            onChange={(e) => setContent(e.target.value)}></textarea>
                    </div>
                </form>

                <button type="button" className="btn btn-success mb-4 me-4" onClick={savePost}>Salvar</button>
                <button type="button" className="btn btn-danger mb-4" hidden={id == null} onClick={deletePost}>Excluir</button>
            </div>

            <Footer/>
        </div>
    );
}