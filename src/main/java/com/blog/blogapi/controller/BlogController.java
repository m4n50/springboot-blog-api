package com.blog.blogapi.controller;

import com.blog.blogapi.DTO.UpdateBlogPostDTO;
import com.blog.blogapi.mapper.BlogPostMapper;
import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.DTO.BlogPostDTO;
import com.blog.blogapi.service.BlogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
@Valid
public class BlogController {

    private final BlogService blogService;
    private final BlogPostMapper blogPostMapper;

    @Autowired
    public BlogController(BlogService blogService, BlogPostMapper blogPostMapper) {
        this.blogService = blogService;
        this.blogPostMapper = blogPostMapper;
    }

    @GetMapping("/posts/page")
    public Page<BlogPost> getPaginatedPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortedBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortedBy));
        return blogService.getPaginatedPosts(pageable);
    }

    @GetMapping("/posts/search")
    public List<BlogPost> searchPosts(@RequestParam String keyword) {
        return blogService.searchPostByTitle(keyword);
    }

    @GetMapping
    public List<BlogPost> getAllPosts() {
        return blogService.getAllPosts();
    }

    @PostMapping("/posts")
    public ResponseEntity<?> addPost(@Valid @RequestBody BlogPostDTO postDTO) {
        BlogPost savedPost = blogService.dtoToEntity(postDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Post created successfully");
        response.put("postId",savedPost.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/posts/{id}")
    public BlogPost getPostById(@PathVariable Long id) {
        return blogService.getPostById(id);
    }

    @GetMapping("/posts/byCategory/{categoryId}")
    public List<BlogPost> getPostsByCategory(@PathVariable Long categoryId){
        return blogService.getPostsByCategory(categoryId);
    }

    @GetMapping("/posts/byAuthor/{authorId}")
    public List<BlogPost> getPostsByAuthor(@PathVariable Long authorId){
        return blogService.getPostsByAuthor(authorId);
    }

    @PutMapping("/posts/{postId}/assignAuthor/{authorId}")
    public BlogPost assignAuthorToPost(@PathVariable Long postId, @PathVariable Long authorId) {
        return blogService.assignAuthor(postId, authorId);
    }

    @PutMapping("/posts/{postId}/assignCategory/{categoryId}")
    public BlogPost assignCategoryToPost(@PathVariable Long postId, @PathVariable Long categoryId) {
        return blogService.assignCategoryToPost(postId, categoryId);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBlogPostDTO updateDTO) {

        BlogPost existingPost = blogService.getPostById(id);

        blogPostMapper.updateEntityFromDTO(updateDTO, existingPost);

        BlogPost updatedPost = blogService.savePost(existingPost);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Post updated successfully");
        response.put("postId", updatedPost.getId());
        response.put("title", updatedPost.getTitle());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/posts/{id}")
    public String deletePost(@PathVariable Long id) {
        blogService.deletePost(id);
        return "Post with id " + id + " deleted successfully.";
    }

    @GetMapping("/summary")
    public List<BlogPostDTO> getAllPostSummaries() {
        return blogService.getAllPostsDTO();
    }

}
