package com.example.index.fantastic_app.controller;

import com.example.index.fantastic_app.domain.Post;
import com.example.index.fantastic_app.repository.PostRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//프론트엔드와 연결되는 REST API 파일
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @PostMapping
    public Post createPost(@RequestBody Post post) {
        return postRepository.save(post);
    }
}
