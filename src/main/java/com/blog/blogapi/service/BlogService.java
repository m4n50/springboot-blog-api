package com.blog.blogapi.service;

import com.blog.blogapi.exception.ResourceNotFoundException;
import com.blog.blogapi.mapper.BlogPostMapper;
import com.blog.blogapi.model.Author;
import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.DTO.BlogPostDTO;
import com.blog.blogapi.model.Category;
import com.blog.blogapi.repository.BlogPostRepository;
import com.blog.blogapi.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // Default: read-only transactions
public class BlogService {

    private final AuthorService authorService;
    private final BlogPostRepository blogPostRepository;
    private final CategoryRepository categoryRepository;
    private final BlogPostMapper blogPostMapper;

    @Autowired
    public BlogService(AuthorService authorService,
                       BlogPostRepository blogPostRepository,
                       CategoryRepository categoryRepository,
                       BlogPostMapper blogPostMapper) {
        this.authorService = authorService;
        this.blogPostRepository = blogPostRepository;
        this.categoryRepository = categoryRepository;
        this.blogPostMapper = blogPostMapper;
    }

    // ========== READ OPERATIONS ==========

    public List<BlogPost> getAllPosts() {
        return blogPostRepository.findAll();
    }

    public Optional<BlogPost> getPostByIdOptional(Long id) {
        return blogPostRepository.findById(id);
    }

    public BlogPost getPostById(Long id) {
        return blogPostRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id " + id));
    }

    public List<BlogPost> getPostsByCategory(Long categoryId) {
        // Validate category exists
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + categoryId));

        return blogPostRepository.findByCategoriesContaining(category);
    }

    public List<BlogPost> getPostsByAuthor(Long authorId) {
        // Validate author exists first
        authorService.getAuthorById(authorId); // This will throw if not found

        return blogPostRepository.findByAuthorId(authorId);
    }

    public List<BlogPost> searchPostsByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty())
            return getAllPosts();

        return blogPostRepository.findByTitleContainingIgnoreCase(keyword.trim());
    }

    public Page<BlogPost> getPaginatedPosts(Pageable pageable) {
        return blogPostRepository.findAll(pageable);
    }

    // ========== WRITE OPERATIONS ==========

    @Transactional
    public BlogPost createPost(BlogPost blogPost) {
        // Business validation
        validatePostForCreation(blogPost);

        // Set creation date if not provided
        if (blogPost.getDate() == null)
            blogPost.setDate(LocalDate.now());

        // Validate and set author
        if (blogPost.getAuthor() != null && blogPost.getAuthor().getId() != null) {
            Author author = authorService.getAuthorById(blogPost.getAuthor().getId());
            blogPost.setAuthor(author);
        }

        // Validate categories
        if (blogPost.getCategories() != null && !blogPost.getCategories().isEmpty()) {
            List<Category> validatedCategories = validateAndGetCategories(blogPost.getCategories());
            blogPost.setCategories(validatedCategories);
        }

        return blogPostRepository.save(blogPost);
    }

    @Transactional
    public Optional<BlogPost> updatePost(Long id, BlogPost updatedPost) {
        return blogPostRepository.findById(id)
                .map(existingPost -> {
                    // Only update non-null fields (partial update pattern)
                    if (updatedPost.getTitle() != null && !updatedPost.getTitle().trim().isEmpty())
                        existingPost.setTitle(updatedPost.getTitle().trim());

                    if (updatedPost.getContent() != null)
                        existingPost.setContent(updatedPost.getContent());

                    if (updatedPost.getDate() != null)
                        existingPost.setDate(updatedPost.getDate());

                    // Update author if provided and valid
                    if (updatedPost.getAuthor() != null && updatedPost.getAuthor().getId() != null) {
                        Author author = authorService.getAuthorById(updatedPost.getAuthor().getId());
                        existingPost.setAuthor(author);
                    }

                    // Update categories if provided
                    if (updatedPost.getCategories() != null) {
                        List<Category> validatedCategories = validateAndGetCategories(updatedPost.getCategories());
                        existingPost.setCategories(validatedCategories);
                    }

                    return blogPostRepository.save(existingPost);
                });
    }

    /**
     * Delete blog post
     */
    @Transactional
    public boolean deletePost(Long id) {
        if (!blogPostRepository.existsById(id)) {
            return false;
        }

        // Business logic: In the future:
        // - Soft delete instead of hard delete
        // - Archive the post
        // - Notify subscribers
        // - Update statistics

        blogPostRepository.deleteById(id);
        return true;
    }

    // ========== BUSINESS OPERATIONS ==========

    @Transactional
    public Optional<BlogPost> assignAuthor(Long postId, Long authorId) {
        return blogPostRepository.findById(postId)
                .map(post -> {
                    Author author = authorService.getAuthorById(authorId);
                    post.setAuthor(author);
                    return blogPostRepository.save(post);
                });
    }

    @Transactional
    public Optional<BlogPost> addCategoryToPost(Long postId, Long categoryId) {
        Optional<BlogPost> postOpt = blogPostRepository.findById(postId);
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);

        if (postOpt.isPresent() && categoryOpt.isPresent()) {
            BlogPost post = postOpt.get();
            Category category = categoryOpt.get();

            if (post.getCategories() == null)
                post.setCategories(new ArrayList<>());

            if (!post.getCategories().contains(category)) {
                post.getCategories().add(category);
                return Optional.of(blogPostRepository.save(post));
            }
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<BlogPost> removeCategoryFromPost(Long postId, Long categoryId) {
        return blogPostRepository.findById(postId)
                .flatMap(post -> categoryRepository.findById(categoryId)
                        .map(category -> {
                            if (post.getCategories() != null) {
                                post.getCategories().remove(category);
                                return blogPostRepository.save(post);
                            }
                            return post;
                        }));
    }

    // ========== DTO OPERATIONS ==========

    /**
     * Get all posts as DTOs
     */
    public List<BlogPostDTO> getAllPostsAsDTO() {
        return blogPostRepository.findAll().stream()
                .map(blogPostMapper::entityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Create post from DTO
     */
    @Transactional
    public BlogPostDTO createPostFromDTO(BlogPostDTO dto) {
        BlogPost post = blogPostMapper.dtoToEntity(dto);
        BlogPost savedPost = createPost(post);
        return blogPostMapper.entityToDto(savedPost);
    }

    // ========== UTILITY/VALIDATION METHODS ==========

    /**
     * Validate post for creation
     */
    private void validatePostForCreation(BlogPost post) {
        if (post.getTitle() == null || post.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Post title is required");
        }

        if (post.getAuthor() == null || post.getAuthor().getId() == null) {
            throw new IllegalArgumentException("Post must have an author");
        }

        if (post.getCategories() == null || post.getCategories().isEmpty()) {
            throw new IllegalArgumentException("Post must have at least one category");
        }
    }

    /**
     * Validate and retrieve categories by their IDs
     */
    private List<Category> validateAndGetCategories(List<Category> categories) {
        List<Long> categoryIds = categories.stream()
                .map(Category::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (categoryIds.isEmpty()) {
            throw new IllegalArgumentException("Invalid category IDs provided");
        }

        List<Category> foundCategories = categoryRepository.findByIdIn(categoryIds);

        if (foundCategories.size() != categoryIds.size()) {
            throw new ResourceNotFoundException("One or more categories not found");
        }

        return foundCategories;
    }

    /**
     * Get total post count
     */
    public long getTotalPostCount() {
        return blogPostRepository.count();
    }

    /**
     * Get posts count by author
     */
    public long getPostCountByAuthor(Long authorId) {
        return blogPostRepository.findByAuthorId(authorId).size();
    }

    /**
     * Search blog posts by title containing keyword (case-insensitive)
     * Uses Spring Data JPA query method
     */
    @Transactional(readOnly = true)
    public List<BlogPost> searchPostByTitle(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Search keyword cannot be null or empty");
        }

        // This assumes you have this method in your BlogPostRepository
        // If not, you'll need to add it there
        return blogPostRepository.findByTitleContainingIgnoreCase(keyword.trim());
    }

    /**
     * Assign a category to a blog post
     * Updates the relationship and saves the post
     */
    @Transactional
    public BlogPost assignCategoryToPost(Long postId, Long categoryId) {
        // Validate input parameters
        if (postId == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }
        if (categoryId == null) {
            throw new IllegalArgumentException("Category ID cannot be null");
        }

        // Find the post - will throw exception if not found
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found with id: " + postId));

        // Find the category - will throw exception if not found
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + categoryId));

        // Assign the category to the post
        post.setCategories((List<Category>) category);

        // Save and return the updated post
        return blogPostRepository.save(post);
    }

    /**
     * Get all blog posts as DTOs (Data Transfer Objects)
     * Converts entities to DTOs using mapper
     */
    @Transactional(readOnly = true)
    public List<BlogPostDTO> getAllPostsDTO() {
        // Get all blog posts from database
        List<BlogPost> allPosts = blogPostRepository.findAll();

        // Convert each BlogPost entity to BlogPostDTO using your mapper
        return allPosts.stream()
                .map(blogPostMapper::toDTO) // Assumes you have a toDTO method in your mapper
                .toList(); // Java 16+ syntax, use .collect(Collectors.toList()) for older versions
    }
}