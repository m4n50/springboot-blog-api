package com.blog.blogapi.service;

import com.blog.blogapi.exception.PostNotFoundException;
import com.blog.blogapi.exception.ResourceNotFoundException;
import com.blog.blogapi.model.Author;
import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.model.BlogPostDTO;
import com.blog.blogapi.model.Category;
import com.blog.blogapi.repository.BlogPostRepository;
import com.blog.blogapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BlogService {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<BlogPost> getAllPosts() {
        return blogPostRepository.findAll();
    }

    public void addPost(BlogPost post) {
        blogPostRepository.save(post);
    }

    public List<BlogPostDTO> getAllPostsDTO() {
        List<BlogPostDTO> dtos = new ArrayList<>();
        for (BlogPost post : blogPostRepository.findAll()) {
            dtos.add(new BlogPostDTO(
                    post.getTitle(),
                    post.getAuthor(),
                    post.getDate(),
                    post.getCategories()
            ));
        }
        return dtos;
    }

    public BlogPost getPostById(Long id) {
        return blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));
    }

    public BlogPost savePost(BlogPost post) {
        return blogPostRepository.save(post);
    }

    public void deletePost(Long id) {
        BlogPost existingPost = blogPostRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id " + id));
        blogPostRepository.delete(existingPost);
    }

    public List<BlogPost> searchPostByTitle(String keyword) {
        return blogPostRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public BlogPost assignAuthor(Long postId, Long authorId) {
        BlogPost post = getPostById(postId);
        Author author = authorService.getAuthorById(authorId);

        post.setAuthor(author);
        return blogPostRepository.save(post);
    }

    public BlogPost assignCategoryToPost(Long postId, Long categoryId) {
        BlogPost post = getPostById(postId);
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));

        if (!post.getCategories().contains(category)) {
            post.getCategories().add(category);
        }

        return blogPostRepository.save(post);
    }

    public Page<BlogPost> getPaginatedPosts(Pageable pageable) {
        return blogPostRepository.findAll(pageable);
    }
}
