package com.blog.blogapi.controller;

import com.blog.blogapi.model.Author;
import com.blog.blogapi.service.AuthorService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Configure properly for production
@Tag(name = "Author Management", description = "APIs for managing blog authors and their information")
public class AuthorController {

    private final AuthorService authorService;

    @Operation(
            summary = "Get all authors",
            description = "Retrieves a list of all registered blog authors with their complete information"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved all authors",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Author.class),
                    examples = @ExampleObject(
                            name = "Authors List",
                            value = """
                [
                    {
                        "id": 1,
                        "name": "John Doe",
                        "email": "john.doe@example.com",
                        "bio": "Experienced software developer with 10+ years in Java and Spring",
                        "createdAt": "2024-01-15T10:30:00",
                        "updatedAt": "2024-01-16T09:45:00"
                    },
                    {
                        "id": 2,
                        "name": "Jane Smith",
                        "email": "jane.smith@example.com",
                        "bio": "Full-stack developer passionate about modern web technologies",
                        "createdAt": "2024-01-18T14:20:00",
                        "updatedAt": "2024-01-18T14:20:00"
                    }
                ]
                """
                    )
            )
    )
    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> authors = authorService.getAllAuthors();
        return ResponseEntity.ok(authors);
    }

    @Operation(
            summary = "Create a new author",
            description = "Creates a new blog author with the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Author created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Creation Success",
                                    value = """
                    {
                        "message": "Author created successfully",
                        "authorId": 3,
                        "name": "Alice Johnson",
                        "email": "alice.johnson@example.com"
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
                        "message": "Email is required and must be valid"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Author with this email already exists",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Conflict Error",
                                    value = """
                    {
                        "error": "Conflict",
                        "message": "Author with email john.doe@example.com already exists"
                    }
                    """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<Map<String, Object>> addAuthor(
            @Parameter(
                    description = "Author data to create",
                    required = true,
                    schema = @Schema(implementation = Author.class)
            )
            @Valid @RequestBody Author author
    ) {
        Author savedAuthor = authorService.createAuthor(author);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Author created successfully");
        response.put("authorId", savedAuthor.getId());
        response.put("name", savedAuthor.getName());
        response.put("email", savedAuthor.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Get author by ID",
            description = "Retrieves a specific author by their unique identifier"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Author found successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Author.class),
                            examples = @ExampleObject(
                                    name = "Author Details",
                                    value = """
                    {
                        "id": 1,
                        "name": "John Doe",
                        "email": "john.doe@example.com",
                        "bio": "Experienced software developer specializing in Spring Boot and microservices",
                        "website": "https://johndoe.dev",
                        "socialMediaLinks": {
                            "twitter": "@johndoe",
                            "linkedin": "linkedin.com/in/johndoe"
                        },
                        "createdAt": "2024-01-15T10:30:00",
                        "updatedAt": "2024-01-16T09:45:00"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Author not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Not Found Error",
                                    value = """
                    {
                        "error": "Author not found",
                        "message": "Author with id 999 does not exist"
                    }
                    """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Author> getAuthorById(
            @Parameter(
                    description = "ID of the author to retrieve",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        Author author = authorService.getAuthorById(id);
        return ResponseEntity.ok(author);
    }

    @Operation(
            summary = "Update author",
            description = "Updates an existing author's information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Author updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Update Success",
                                    value = """
                    {
                        "message": "Author updated successfully",
                        "authorId": 1,
                        "name": "John Doe Updated",
                        "email": "john.doe.updated@example.com"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Author not found"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAuthor(
            @Parameter(
                    description = "ID of the author to update",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id,

            @Parameter(
                    description = "Updated author data",
                    required = true,
                    schema = @Schema(implementation = Author.class)
            )
            @Valid @RequestBody Author author
    ) {
        Author updatedAuthor = authorService.updateAuthor(id, author);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Author updated successfully");
        response.put("authorId", updatedAuthor.getId());
        response.put("name", updatedAuthor.getName());
        response.put("email", updatedAuthor.getEmail());

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Delete author",
            description = "Permanently removes an author from the system. Note: This will also affect associated blog posts."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Author deleted successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Delete Success",
                                    value = """
                    {
                        "message": "Author with id 1 deleted successfully"
                    }
                    """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Author not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Cannot delete author - has associated blog posts",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Conflict Error",
                                    value = """
                    {
                        "error": "Conflict",
                        "message": "Cannot delete author. Author has 5 associated blog posts."
                    }
                    """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteAuthor(
            @Parameter(
                    description = "ID of the author to delete",
                    required = true,
                    example = "1"
            )
            @PathVariable Long id
    ) {
        authorService.deleteAuthor(id);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Author with id " + id + " deleted successfully");

        return ResponseEntity.ok(response);
    }

    // BONUS: Additional professional endpoints

    @Operation(
            summary = "Get authors count",
            description = "Returns the total number of registered authors in the system"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Authors count retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Authors Count",
                            value = """
                {
                    "totalAuthors": 12
                }
                """
                    )
            )
    )
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getAuthorsCount() {
        long count = authorService.getAuthorsCount();

        Map<String, Long> response = new HashMap<>();
        response.put("totalAuthors", count);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Search authors by name",
            description = "Search for authors by name using partial matching (case-insensitive)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search completed successfully",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Author.class)
            )
    )
    @GetMapping("/search")
    public ResponseEntity<List<Author>> searchAuthors(
            @Parameter(
                    description = "Name or partial name to search for",
                    required = true,
                    example = "john"
            )
            @RequestParam String name
    ) {
        List<Author> authors = authorService.searchAuthorsByName(name);
        return ResponseEntity.ok(authors);
    }

    @Operation(
            summary = "Check if author email exists",
            description = "Checks whether an author with the given email already exists (useful for frontend validation)"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Check completed successfully",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Email Exists Response",
                            value = """
                {
                    "exists": true
                }
                """
                    )
            )
    )
    @GetMapping("/exists")
    public ResponseEntity<Map<String, Boolean>> authorEmailExists(
            @Parameter(
                    description = "Email address to check for existence",
                    required = true,
                    example = "john.doe@example.com"
            )
            @RequestParam String email
    ) {
        boolean exists = authorService.authorExistsByEmail(email);

        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);

        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get authors with post counts",
            description = "Retrieves all authors along with their respective blog post counts"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved authors with post counts",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "Authors with Post Counts",
                            value = """
                [
                    {
                        "id": 1,
                        "name": "John Doe",
                        "email": "john.doe@example.com",
                        "bio": "Experienced developer",
                        "postCount": 15
                    },
                    {
                        "id": 2,
                        "name": "Jane Smith",
                        "email": "jane.smith@example.com",
                        "bio": "Full-stack developer",
                        "postCount": 8
                    }
                ]
                """
                    )
            )
    )
    @GetMapping("/with-counts")
    public ResponseEntity<List<Map<String, Object>>> getAuthorsWithPostCounts() {
        List<Map<String, Object>> authorsWithCounts = authorService.getAuthorsWithPostCounts();
        return ResponseEntity.ok(authorsWithCounts);
    }
}