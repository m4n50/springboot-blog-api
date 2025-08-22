package com.blog.blogapi.controller;

import com.blog.blogapi.DTO.UpdateBlogPostDTO;
import com.blog.blogapi.mapper.BlogPostMapper;
import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.DTO.BlogPostDTO;
import com.blog.blogapi.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Blog Post Management", description = "APIs for managing blog posts, including CRUD operations, search, pagination, and relationship management")
public class BlogController {

    private final BlogService blogService;
    private final BlogPostMapper blogPostMapper;

    @Operation(
            summary = "Get paginated posts with sorting",
            description = "Retrieves blog posts with pagination and sorting capabilities. Supports sorting by any field (id, title, createdAt, etc.)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved paginated posts",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Paginated Posts Response",
                                    value = """
                    {
                        "content": [
                            {
                                "id": 1,
                                "title": "Introduction to Spring Boot",
                                "content": "Spring Boot makes it easy to create stand-alone...",
                                "createdAt": "2024-01-15T10:30:00",
                                "author": {"id": 1, "name": "John Doe"},
                                "category": {"id": 1, "name": "Technology"}
                            }
                        ],
                        "pageable": {
                            "sort": {"sorted": true, "by": ["title"]},
                            "pageNumber": 0,
                            "pageSize": 5
                        },
                        "totalElements": 25,
                        "totalPages": 5,
                        "last": false,
                        "first": true,
                        "numberOfElements": 5
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination parameters"
            )
    })
    @GetMapping("/page")
    public ResponseEntity<Page<BlogPost>> getPaginatedPosts(
            @Parameter(
                    description = "Page number (0-based)",
                    example = "0"
            )
            @RequestParam(defaultValue = "0") int page,

            @Parameter(
                    description = "Number of posts per page",
                    example = "5"
            )
            @RequestParam(defaultValue = "5") int size,

            @Parameter(
                    description = "Field to sort by (id, title, createdAt, etc.)",
                    example = "title"
            )
            @RequestParam(defaultValue = "id") String sortedBy
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortedBy));
        Page<BlogPost> posts = blogService.getPaginatedPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    @Operation(
            summary = "Search posts by title",
            description = "Search for blog posts using a keyword that matches the title (case-insensitive partial matching)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search completed successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BlogPost.class),
                    examples = @ExampleObject(
                            name = "Search Results",
                            value = """
                [
                    {
                        "id": 1,
                        "title": "Spring Boot Tutorial",
                        "content": "Learn Spring Boot basics...",
                        "createdAt": "2024-01-15T10:30:00"
                    },
                    {
                        "id": 5,
                        "title": "Advanced Spring Concepts",
                        "content": "Deep dive into Spring...",
                        "createdAt": "2024-01-20T14:15:00"
                    }
                ]
                """
                    )
            )
    )
    @GetMapping("/search")
    public ResponseEntity<List<BlogPost>> searchPosts(
            @Parameter(
                    description = "Keyword to search for in post titles",
                    required = true,
                    example = "spring"
            )
            @RequestParam String keyword
    ) {
        List<BlogPost> posts = blogService.searchPostByTitle(keyword);
        return ResponseEntity.ok(posts);
    }

    @Operation(
            summary = "Create a new blog post",
            description = "Creates a new blog post with the provided content and metadata"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Post created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Creation Success",
                                    value = """
                    {
                        "message": "Post created successfully",
                        "postId": 10,
                        "title": "My New Blog Post"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Validation Error",
                                    value = """
                    {
                        "error": "Validation failed",
                        "message": "Title cannot be empty"
                    }
                    """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> createPost(
            @Parameter(
                    description = "Blog post data to create",
                    required = true,
                    schema = @Schema(implementation = BlogPost.class)
            )
            @Valid @RequestBody BlogPost post
    ) {
        BlogPost savedPost = blogService.createPost(post);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Post created successfully");
        response.put("postId", savedPost.getId());
        response.put("title", savedPost.getTitle());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get post by ID",
            description = "Retrieves a specific blog post by its unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Post found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BlogPost.class),
                            examples = @ExampleObject(
                                    name = "Blog Post Details",
                                    value = """
                    {
                        "id": 1,
                        "title": "Introduction to Microservices",
                        "content": "Microservices architecture is a method of developing software systems...",
                        "createdAt": "2024-01-15T10:30:00",
                        "updatedAt": "2024-01-16T09:45:00",
                        "author": {
                            "id": 1,
                            "name": "Jane Smith",
                            "email": "jane@example.com"
                        },
                        "category": {
                            "id": 2,
                            "name": "Architecture",
                            "description": "Software architecture patterns"
                        }
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<BlogPost> getPostById(
            @Parameter(
                    description = "ID of the post to retrieve",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        BlogPost post = blogService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @Operation(
            summary = "Get posts by category",
            description = "Retrieves all blog posts that belong to a specific category"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Posts retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BlogPost.class)
            )
    )
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<BlogPost>> getPostsByCategory(
            @Parameter(
                    description = "ID of the category to filter posts by",
                    required = true,
                    example = "1"
            )
            @PathVariable Long categoryId
    ) {
        List<BlogPost> posts = blogService.getPostsByCategory(categoryId);
        return ResponseEntity.ok(posts);
    }

    @Operation(
            summary = "Get posts by author",
            description = "Retrieves all blog posts written by a specific author"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Posts retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BlogPost.class)
            )
    )
    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<BlogPost>> getPostsByAuthor(
            @Parameter(
                    description = "ID of the author to filter posts by",
                    required = true,
                    example = "1"
            )
            @PathVariable Long authorId
    ) {
        List<BlogPost> posts = blogService.getPostsByAuthor(authorId);
        return ResponseEntity.ok(posts);
    }

    @Operation(
            summary = "Assign author to post",
            description = "Associates an existing author with an existing blog post"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Author assigned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BlogPost.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post or author not found"
            )
    })
    @PutMapping("/{postId}/author/{authorId}")
    public ResponseEntity<Optional<BlogPost>> assignAuthorToPost(
            @Parameter(
                    description = "ID of the post to update",
                    required = true,
                    example = "1"
            )
            @PathVariable Long postId,

            @Parameter(
                    description = "ID of the author to assign",
                    required = true,
                    example = "2"
            )
            @PathVariable Long authorId
    ) {
        Optional<BlogPost> updatedPost = blogService.assignAuthor(postId, authorId);
        return ResponseEntity.ok(updatedPost);
    }

    @Operation(
            summary = "Assign category to post",
            description = "Associates an existing category with an existing blog post"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Category assigned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BlogPost.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post or category not found"
            )
    })
    @PutMapping("/{postId}/category/{categoryId}")
    public ResponseEntity<BlogPost> assignCategoryToPost(
            @Parameter(
                    description = "ID of the post to update",
                    required = true,
                    example = "1"
            )
            @PathVariable Long postId,

            @Parameter(
                    description = "ID of the category to assign",
                    required = true,
                    example = "2"
            )
            @PathVariable Long categoryId
    ) {
        BlogPost updatedPost = blogService.assignCategoryToPost(postId, categoryId);
        return ResponseEntity.ok(updatedPost);
    }

    @Operation(
            summary = "Update existing post",
            description = "Updates an existing blog post with new content and metadata"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Post updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Update Success",
                                    value = """
                    {
                        "message": "Post updated successfully",
                        "postId": 1,
                        "title": "Updated Post Title"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePost(
            @Parameter(
                    description = "ID of the post to update",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Updated post data",
                    required = true,
                    schema = @Schema(implementation = UpdateBlogPostDTO.class)
            )
            @Valid @RequestBody UpdateBlogPostDTO updateDTO
    ) {
        BlogPost existingPost = blogService.getPostById(id);
        blogPostMapper.updateEntityFromDTO(updateDTO, existingPost);
        BlogPost updatedPost = blogService.createPost(existingPost);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Post updated successfully");
        response.put("postId", updatedPost.getId());
        response.put("title", updatedPost.getTitle());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete post",
            description = "Permanently removes a blog post from the system"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Post deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Delete Success",
                                    value = """
                    {
                        "message": "Post with id 1 deleted successfully"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Post not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletePost(
            @Parameter(
                    description = "ID of the post to delete",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        blogService.deletePost(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Post with id " + id + " deleted successfully");

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get all post summaries",
            description = "Retrieves a simplified view (DTO) of all blog posts with essential information only"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Post summaries retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BlogPostDTO.class),
                    examples = @ExampleObject(
                            name = "Post Summaries",
                            value = """
                [
                    {
                        "id": 1,
                        "title": "Spring Boot Basics",
                        "summary": "A brief introduction to Spring Boot framework...",
                        "author": "John Doe",
                        "category": "Technology",
                        "publishedDate": "2024-01-15"
                    },
                    {
                        "id": 2,
                        "title": "Docker Containerization",
                        "summary": "Learn how to containerize your applications...",
                        "author": "Jane Smith",
                        "category": "DevOps",
                        "publishedDate": "2024-01-18"
                    }
                ]
                """
                    )
            )
    )
    @GetMapping("/summaries")
    public ResponseEntity<List<BlogPostDTO>> getAllPostSummaries() {
        List<BlogPostDTO> summaries = blogService.getAllPostsDTO();
        return ResponseEntity.ok(summaries);
    }

    // BONUS: Additional professional endpoints

    @Operation(
            summary = "Get all posts",
            description = "Retrieves a simple list of all blog posts (without pagination)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "All posts retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BlogPost.class)
            )
    )
    @GetMapping
    public ResponseEntity<List<BlogPost>> getAllPosts() {
        // You'd need to add this method to your BlogService
        // List<BlogPost> posts = blogService.getAllPosts();
        // return ResponseEntity.ok(posts);

        // For now, redirect to summaries
        List<BlogPostDTO> summaries = blogService.getAllPostsDTO();
        return ResponseEntity.ok(Collections.emptyList()); // Placeholder
    }

    @Operation(
            summary = "Get posts count",
            description = "Returns the total number of blog posts in the system"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Posts count retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Posts Count",
                            value = """
                {
                    "totalPosts": 42
                }
                """
                    )
            )
    )
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getPostsCount() {
        // You'd add this to your BlogService
        // long count = blogService.getPostsCount();

        Map<String, Long> response = new HashMap<>();
        response.put("totalPosts", 0L); // Placeholder - implement in service

        return ResponseEntity.ok(response);
    }
}