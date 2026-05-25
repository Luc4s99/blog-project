import './App.css'

import { useState, useEffect } from 'react'

import Navbar from './components/navbar/Navbar'
import Footer from './components/footer/Footer'
import api from "./services/api"

import { Link } from "react-router-dom"
import type { Post } from './models/Post'
import { getUser } from './services/authService'

import { FaRegCommentAlt } from "react-icons/fa";

export default function App() {

  const [posts, setPosts] = useState([]);

  useEffect(() => {
    
    const loadPosts = async () => {

      const response = await api.get("/posts")
      setPosts(response.data)
    }

    loadPosts();
  }, [posts]);

  const verifyAuthor = (authorId: string | undefined) => {

    if(authorId == null) {

      return false;
    }

    const loggedUser = getUser();

    if(loggedUser) {

      return loggedUser.id !== authorId;
    }

    return false;
    
  }

  return (
    <div className='blogBody'>
      <Navbar />
      <div className='container'>
        
        {
          posts.length > 0 ?

          posts.map( (post: Post) => (

            <div className='card-box' key={post.id}>

              <div className="card text-bg-dark mb-3 post-card">

                <div className="card-header">

                  <div className='commentHeaderBox'>Post</div>

                  <div className='text-end commentHeaderBox'>

                    <FaRegCommentAlt /> {post.comments ? post.comments.length : 0}

                  </div>
            
                </div>

                <div className="card-body">

                  <h5 className="card-title">{post.title}</h5>

                  <p className="card-text">Autor: {post.author.login}</p>

                  <Link to={`/posts/${post.id}`} className="btn btn-primary me-2">Ver Post</Link>
                  <Link to={`/editar/${post.id}`} hidden={verifyAuthor(post.author.id)} className="btn btn-secondary">Editar Post</Link>

                </div>

              </div>
              
            </div>
          ))

          :

          <h5 className="card-title mt-5">Nenhum post encontrado.</h5>
        }
        
      </div>
      <Footer/>
    </div>
  )
}
