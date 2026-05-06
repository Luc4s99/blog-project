import '../index.css'

import Footer from "../components/footer/Footer";
import Navbar from "../components/navbar/Navbar";
import { useSearchParams, Link } from 'react-router-dom';
import { useEffect, useState } from 'react';
import api from '../services/api';
import type { Post } from '../models/Post';

export default function PostSearch() {

    const [searchCriteria] = useSearchParams();
    const [posts, setPosts] = useState([])

    useEffect(() => {

        const search = async () => {

            const criteria = searchCriteria.get('title');
            const response = await api.get(`/posts?title=${criteria}`)

            setPosts(response.data);
        }

        search();
    }, [searchCriteria]);

    return (

        <div className='blogBody'>
        
            <Navbar/>

                <div className="container">

                    <h3 className='mt-2'>Resultados da pesquisa:</h3>

                    <ul className="list-group pt-4">

                        {
                            posts?.length === 0 ? (
                                
                                <li className="list-group-item">

                                    Nenhum resultado encontrado!

                                </li>
                            ) : (

                                posts.map((post: Post) => (

                                    <li className="list-group-item">
                                        
                                        {post.title}
                                        <Link to={`/posts/${post.id}`} className="btn btn-primary float-end">Ver Post</Link>
                                    </li>
                                ))
                            )
                        }
                    </ul>

                </div>

            <Footer/>

        </div>
    );
}