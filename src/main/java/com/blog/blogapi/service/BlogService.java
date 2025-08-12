package com.blog.blogapi.service;

import com.blog.blogapi.exception.PostNotFoundException;
import com.blog.blogapi.exception.ResourceNotFoundException;
import com.blog.blogapi.mapper.BlogPostMapper;
import com.blog.blogapi.model.Author;
import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.DTO.BlogPostDTO;
import com.blog.blogapi.model.Category;
import com.blog.blogapi.repository.BlogPostRepository;
import com.blog.blogapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogService {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private final BlogPostMapper blogPostMapper;

    public BlogService(AuthorService authorService,
                       BlogPostRepository blogPostRepository,
                       CategoryRepository categoryRepository,
                       BlogPostMapper blogPostMapper){
        this.authorService = authorService;
        this.blogPostRepository = blogPostRepository;
        this.categoryRepository = categoryRepository;
        this.blogPostMapper = blogPostMapper;
    };

    public List<BlogPost> getAllPosts() {
        return blogPostRepository.findAll();
    }

//    public BlogPostDTO addPost(BlogPostDTO dto) {
//        BlogPost post = blogPostMapper.dtoToEntity(dto);
//        BlogPost saved = blogPostRepository.save(post);
//        return blogPostMapper.entityToDto(saved);
//    }

    public BlogPost addPost(BlogPost blogPost){
        return blogPostRepository.save(blogPost);
    }

    public BlogPost savePost(BlogPost post) {
        return blogPostRepository.save(post);
    }

    public BlogPost getPostById(Long id) {
        return blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));
    }

    public List<BlogPost> getPostsByCategory(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));

        return blogPostRepository.findByCategoriesContaining(category);
    }

    public List<BlogPost> getPostsByAuthor(Long authorId){
        return blogPostRepository.findByAuthorId(authorId);
    }

    public BlogPost updatePost(Long id, BlogPost updatedPost){
        BlogPost existingPost = getPostById(id);
        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());
        existingPost.setCategories(updatedPost.getCategories());
        existingPost.setAuthor(updatedPost.getAuthor());
        return blogPostRepository.save(existingPost);
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

    public List<BlogPostDTO> getAllPostsDTO(){
        return blogPostRepository.findAll().stream()
                .map(blogPostMapper::entityToDto)
                .collect(Collectors.toList());
    }
}
