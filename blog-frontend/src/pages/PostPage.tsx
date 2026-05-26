import Footer from "../components/footer/Footer";
import Navbar from "../components/navbar/Navbar";
import { useEffect, useState } from 'react';
import api from "../services/api";
import { useParams, useNavigate } from 'react-router-dom';
import type { Post } from '../models/Post';
import type { Comment } from '../models/Comment';
import { getUser } from '../services/authService';
import { v4 as uuid } from 'uuid';
import { toast } from 'react-toastify';

export default function PostPage() {

    const [editedCommentId, setEditedCommentId] = useState('');
    const [editedCommentContent, setEditedCommentContent] = useState('');
    const [commentContent, setCommentContent] = useState('');
    const [postComments, setPostComments] = useState<Comment[]>();
    const [post, setPost] = useState<Post>();
    const navigate = useNavigate();

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

        //Como os comentários são atrelados ao post, para salvar um comentário deve ser atualizado o post junto
        const updatedPost = {

            id: post?.id,
            title: post?.title,
            author: post?.author.id,
            content: post?.content,
            createdAt: post?.createdAt,
            comments: post?.comments
        }

        const newComment: Comment = {

            id: uuid(),
            content: commentContent,
            createdAt: new Date(),
            username: getUser()?.login ?? 'Anônimo',
            edited: false
            
        }

        if(updatedPost.comments == null) {

            updatedPost.comments = []
        }
        
        updatedPost.comments.push(newComment);

        api.put('/posts', updatedPost);

        setCommentContent('');
    }

    const deletePostComment = async (commentId: string) => {

        const updatedComments: Comment[] = [];

        if(post && post.comments) {

            post.comments.map((comment) => {

                if(comment.id !== commentId) {

                    updatedComments.push(comment);
                }
            });

            setPostComments(updatedComments);

            await api.put("/posts",
                {
                    id: post.id,
                    title: post.title, 
                    author: post.author.id,
                    content: post.content,
                    createdAt: post.createdAt,  
                    comments: updatedComments
                }
            );
        }
    }

    const editPostComment = async () => {

        const updatedComments: Comment[] = [];

        if(post && post.comments) {

            post.comments.map((comment) => {

                if(comment.id === editedCommentId) {

                    comment.edited = true;
                    comment.content = editedCommentContent;
                }

                updatedComments.push(comment);
            });

            setPostComments(updatedComments);

            await api.put("/posts",
                {
                    id: post.id,
                    title: post.title, 
                    author: post.author.id,
                    content: post.content,
                    createdAt: post.createdAt,  
                    comments: updatedComments
                }
            );

            toast.success('Comentário editado com sucesso!');
        }
    }

    const setModalVariables = async (commentId: string, commentContent: string) => {

        setEditedCommentId(commentId);
       setEditedCommentContent(commentContent);
    }

    const verifyAuthor = (username: string) => {

        if(!username) {

            return false;
        }

        const loggedUser = getUser();

        if(loggedUser) {

            return loggedUser.login !== username;
        }

        return false;

    }

    const goBack = () => {

        navigate('/home');
    }

    return(
        <>
            <div className='blogBody'>
                <Navbar/>

                <div className='mb-2 container'>

                    <button type="button" className="btn btn-secondary mb-2 mt-2" onClick={goBack}>Voltar</button>

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

                                <div className='mb-3 commentBox' key={comment.id}>
                                    <h5 className='ms-4'>{comment.edited ? (comment.username + ' (editado)') : comment.username}:</h5>
                                    <p className='ms-5'>{comment.content}</p>

                                    <div className="commentButtonBox">
                                        <button type="button" className="btn btn-secondary mb-1 me-2 btn-sm" data-bs-toggle="modal" data-bs-target="#editCommentContentModal" hidden={verifyAuthor(comment.username)} onClick={() => setModalVariables(comment.id, comment.content)}>Editar</button>
                                        <button type="button" className="btn btn-danger mb-1 me-2 btn-sm" hidden={verifyAuthor(comment.username)} onClick={() => deletePostComment(comment.id)}>Excluir</button>
                                    </div>
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

            <div className="modal fade" id="editCommentContentModal" aria-labelledby="editCommentContentModalLabel" aria-hidden="true">
                <div className="modal-dialog">
                    <div className="modal-content">
                    <div className="modal-header">
                        <h1 className="modal-title fs-5" id="editCommentContentModalLabel">Editar Comentário</h1>
                        <button type="button" className="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div className="modal-body">
                        <div className="mb-3">
                            <label htmlFor="editedText" className="form-label">Deixe seu comentário sobre essa publicação:</label>
                            <textarea className="form-control" value={editedCommentContent} id="editedText" rows={3} 
                                onChange={(e) => setEditedCommentContent(e.target.value)}></textarea>
                        </div>
                    </div>
                    <div className="modal-footer">
                        <button type="button" className="btn btn-secondary" data-bs-dismiss="modal">Fechar</button>
                        <button type="button" className="btn btn-success" data-bs-dismiss="modal" onClick={editPostComment}>Salvar</button>
                    </div>
                    </div>
                </div>
            </div>
        </>
        
    );
}