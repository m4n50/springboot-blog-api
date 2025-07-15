package com.blog.blogapi.controller;

import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.model.BlogPostDTO;
import com.blog.blogapi.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/posts")
public class BlogController {
    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService){
        this.blogService = blogService;
    }

    @GetMapping
    public List<BlogPost> getAllPosts(){
        return blogService.getAllPosts();
    }

    @PostMapping
    public void addPost(@RequestBody BlogPost post){
        blogService.addPost(post);
    }

    @GetMapping("/summary")
    public List<BlogPostDTO> getAllPostSummaries(){
        return blogService.getAllPostsDTO();
    }
}

