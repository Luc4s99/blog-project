import Footer from "../components/footer/Footer";
import Navbar from "../components/navbar/Navbar";
import { useEffect, useState } from 'react';
import api from "../services/api";
import { useParams } from 'react-router-dom';
import type { Post } from '../models/Post';
import type { Comment } from '../models/Comment';
import { getUser } from '../services/authService';

export default function PostPage() {

    const [commentContent, setCommentContent] = useState('');
    const [postComments, setPostComments] = useState<Comment[]>();
    const [post, setPost] = useState<Post>()

    const { id } = useParams();

    useEffect(() => {

        const loadPost = async () => {

            const response = await api.get(`/posts/${id}`);
            const postReceived: Post = response.data;

            setPost(postReceived);
            setPostComments(postReceived.comments);
        }

        loadPost();
    }, [id, postComments]);

    const saveComment = () => {

        //const updatedPost = structuredClone(post);
        const updatedPost = {

            id: post?.id,
            title: post?.title,
            author: post?.author.id,
            content: post?.content,
            createdAt: post?.createdAt,
            comments: post?.comments
        }

        const newComment: Comment = {

            content: commentContent,
            createdAt: new Date(),
            username: getUser()?.login ?? 'Anônimo'
            
        }

        if(updatedPost.comments == null) {

            updatedPost.comments = []
        }
        
        updatedPost.comments.push(newComment);

        api.put('/posts', updatedPost);

        setCommentContent('');
    }

    return(
        <div className='blogBody'>
            <Navbar/>

            <div className='mb-2 container'>
                <h6 className='mt-2'>Por: {post?.author?.login}</h6>

                <h2 className='text-center mt-3'>{post?.title}</h2>

                <div className='contentContainer'>
                    <p className='text-justify mt-2 mb-4'>{post?.content}</p>
                </div>

                <hr />

                <div className=" mt-5 commentsContainer">

                    <h5 className='mb-4'>Comentários: {postComments ? postComments.length : 0}</h5>

                    {
                        postComments?.map((comment: Comment) => (

                            <div className='commentBox'>
                                <h6 className='ms-4'>{comment.username}:</h6>
                                <p className='ms-5'>{comment.content}</p>
                            </div>
                        ))
                    }

                    <hr />

                    <div className="mb-3">
                        <label htmlFor="contentText" className="form-label">Deixe seu comentário sobre essa publicação:</label>
                        <textarea className="form-control" value={commentContent} id="contentText" rows={3} 
                            onChange={(e) => setCommentContent(e.target.value)}></textarea>
                    </div>

                    <button type="button" disabled={!commentContent} className="btn btn-success mb-4 me-4" onClick={saveComment}>Comentar</button>
                    
                </div>
                
            </div>

            <Footer/>
        </div>
    );
}