package com.blog.blogapi.service;

import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.model.BlogPostDTO;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BlogService {
    private List<BlogPost> posts = new ArrayList<>();

    public BlogService(){}

    public List<BlogPost> getAllPosts(){
        return posts;
    }

    public void addPost(BlogPost post){
        posts.add(post);
    }

    public List<BlogPostDTO> getAllPostsDTO(){
        List<BlogPostDTO> dtos = new ArrayList<>();
        for (BlogPost post : posts){
            dtos.add(new BlogPostDTO(
                    post.getTitle(),
                    post.getAuthor(),
                    post.getDate(),
                    post.getCategories()
            ));
        }
        return dtos;
    }

}
