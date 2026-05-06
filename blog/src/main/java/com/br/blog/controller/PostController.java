package com.br.blog.controller;

import com.br.blog.dto.response.PostDto;
import com.br.blog.entity.Post;
import com.br.blog.service.PostService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {

        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getPosts() {

        List<PostDto> results = postService.getAllPostsWithAuthor();

        return ResponseEntity.status(HttpStatus.OK).body(results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable String id) {

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostById(id));
    }

    @GetMapping(params = "comments")
    public ResponseEntity<List<Post>> getPostsWithComments(@RequestParam Boolean comments) {

        if (comments) {

            return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsWithComments());
        }else {

            return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsWithNoComments());
        }
    }

    @GetMapping(params = "firstDate")
    public ResponseEntity<List<Post>> getPostsByComments(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date firstDate,
                                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date lastDate) {

        List<Post> foundPosts = postService.getPostByDate(firstDate, lastDate);

        if (foundPosts.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else {

            return ResponseEntity.status(HttpStatus.OK).body(foundPosts);
        }
    }

    @GetMapping(params = "author")
    public ResponseEntity<List<Post>> getPostsByAuthor(String author) {

        List<Post> foundPosts = postService.getPostsByAuthor(author);

        if (foundPosts.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else {

            return ResponseEntity.status(HttpStatus.OK).body(foundPosts);
        }
    }

    @GetMapping(params = "title")
    public ResponseEntity<List<Post>> getPostsByTitle(String title) {

        List<Post> foundPosts = postService.getPostsByTitle(title);

        if (foundPosts.isEmpty()) {

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }else {

            return ResponseEntity.status(HttpStatus.OK).body(foundPosts);
        }
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post) {

        return ResponseEntity.status(HttpStatus.CREATED).body(postService.addPost(post));
    }

    @PutMapping
    public ResponseEntity<Post> updatePost(@RequestBody Post post) {

        return ResponseEntity.status(HttpStatus.OK).body(postService.updatePost(post));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePost(@RequestParam String post) {

        postService.deletePostById(post);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
