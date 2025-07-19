package com.blog.blogapi.service;

import com.blog.blogapi.exception.PostNotFoundException;
import com.blog.blogapi.exception.ResourceNotFoundException;
import com.blog.blogapi.model.Author;
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
    private AuthorService authorService;

    @Autowired
    private BlogPostRepository blogPostRepository;

    public BlogService(){}

    public List<BlogPost> getAllPosts(){
        return blogPostRepository.findAll();
    }

    public void addPost(BlogPost post){
        blogPostRepository.save(post);
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
        return blogPostRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Post not found with id " + id));
    }

    public BlogPost savePost(BlogPost post){
        return blogPostRepository.save(post);
    }

    public void deletePost(int id){
        BlogPost existingPost = blogPostRepository.findById(id).orElse(null);
        if(existingPost == null)
            throw new PostNotFoundException("Post not found with id " + id);
        blogPostRepository.delete(existingPost);
    }

    public List<BlogPost> searchPostByTitle(String keyword){
        return blogPostRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public BlogPost assignAuthor(int postId, int authorId){
        BlogPost post = getPostById(postId);
        Author author = authorService.getAuthorById(authorId);

        if(post == null)
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        if(author == null)
            throw new ResourceNotFoundException("Author not found with id: " + authorId);

        post.setAuthor(author);
        return blogPostRepository.save(post);
    }
}
