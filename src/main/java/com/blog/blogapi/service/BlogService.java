package com.blog.blogapi.service;

import com.blog.blogapi.exception.PostNotFoundException;
import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.model.BlogPostDTO;
import com.blog.blogapi.repository.BlogPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BlogService {
    private List<BlogPost> posts = new ArrayList<>();

    @Autowired
    private BlogPostRepository repository;

    public BlogService(){}

    public List<BlogPost> getAllPosts(){
        return repository.findAll();
    }

    public void addPost(BlogPost post){
        repository.save(post);
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

    public BlogPost getPostById(int id){
        return repository.findById(id).orElse(null);
    }

    public BlogPost savePost(BlogPost post){
        return repository.save(post);
    }

    public void deletePost(int id){
        BlogPost existingPost = repository.findById(id).orElse(null);
        if(existingPost == null)
            throw new PostNotFoundException("Post not found with id " + id);
        repository.delete(existingPost);
    }

    public List<BlogPost> searchPostByTitle(String keyword){
        return repository.findByTitleContainingIgnoreCase(keyword);
    }
}
