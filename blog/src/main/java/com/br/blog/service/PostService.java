package com.br.blog.service;

import com.br.blog.dto.response.PostDto;
import com.br.blog.entity.Post;
import com.br.blog.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {

        this.postRepository = postRepository;
    }

    public List<PostDto> getAllPostsWithAuthor() {

        return postRepository.getAllPostsWithAuthor();
    }

    public PostDto getPostById(String id) {

        return postRepository.getPostById(id).orElse(null);
    }

    public List<Post> getPostsWithNoComments() {

        return postRepository.getPostsWithNoComments();
    }

    public List<Post> getPostsWithComments() {

        return postRepository.getPostsWithComments();
    }

    public List<Post> getPostByDate(Date firstDate, Date lastDate) {

        return postRepository.getPostsByDate(firstDate, lastDate);
    }

    public List<Post> getPostsByAuthor(String author) {

        return postRepository.getPostsByAuthor(author);
    }

    public List<Post> getPostsByTitle(String title) {

        return postRepository.getPostsByTitle(title);
    }

    public Post addPost(Post post) {

        return postRepository.save(post);
    }

    public Post updatePost(Post post) {

        return postRepository.save(post);
    }

    public void  deletePostById(String postId) {

        postRepository.deleteById(postId);
    }
}
