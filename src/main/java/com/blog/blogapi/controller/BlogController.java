package com.blog.blogapi.controller;

import com.blog.blogapi.exception.ResourceNotFoundException;
import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.model.BlogPostDTO;
import com.blog.blogapi.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class BlogController {
    private final BlogService blogService;

    @Autowired
    public BlogController(BlogService blogService){
        this.blogService = blogService;
    }

    @GetMapping("/posts/search")
    public List<BlogPost> searchPosts(@RequestParam String keyword){
        return blogService.searchPostByTitle(keyword);
    }

    @GetMapping("/posts")
    public List<BlogPost> getAllPosts(){
        return blogService.getAllPosts();
    }

    @PostMapping("/posts")
    public void addPost(@RequestBody BlogPost post){
        blogService.addPost(post);
    }

    @GetMapping("/posts/{id}")
    public BlogPost getPostById(@PathVariable int id){
        return blogService.getPostById(id);
    }


    @PutMapping("/posts/{postId}/assignAuthor/{authorId}")
    public BlogPost assignAuthorToPost(@PathVariable int postId, @PathVariable int authorId){
        return blogService.assignAuthor(postId, authorId);
    }

    @PutMapping("/posts/{id}")
    public BlogPost updatePost(@PathVariable int id, @RequestBody BlogPost updatedPost){
        BlogPost existingPost = blogService.getPostById(id);

        if(existingPost == null)
            throw new ResourceNotFoundException("Post not found with id: " + id);

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        // Add other fields like date, author, categories as needed

        return blogService.savePost(existingPost);
    }

    @DeleteMapping("/posts/{id}")
    public String deletePost(@PathVariable int id){
        blogService.deletePost(id);
        return "Post with id " + id + " deleted successfully.";
    }

    @GetMapping("/summary")
    public List<BlogPostDTO> getAllPostSummaries(){
        return blogService.getAllPostsDTO();
    }

}

