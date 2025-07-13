package com.blog.blogapi.controller;

import com.blog.blogapi.model.BlogPost;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("api/posts")
public class BlogController {
    private List<BlogPost> posts = new ArrayList<>();

    // Dummy data for testing
    public BlogController(){
        posts.add(new BlogPost(1, "First Post", "Hello World",
                LocalDate.now(), "Admin", Arrays.asList("General")));
    }

    @GetMapping
    public List<BlogPost> getAllPosts(){
        return posts;
    }
}
