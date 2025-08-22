package com.blog.blogapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Author entity representing blog authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier for the author", example = "1")
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Author name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Schema(description = "Full name of the author", example = "John Doe")
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    @Schema(description = "Email address of the author (must be unique)", example = "john.doe@example.com")
    private String email;

    @OneToMany(mappedBy = "author")
    @JsonManagedReference
    private List<BlogPost> posts;

    // BIOGRAPHY FIELD - Optional, longer text
    @Column(name = "bio", columnDefinition = "TEXT") // TEXT for longer content
    @Size(max = 1000, message = "Biography cannot exceed 1000 characters")
    @Schema(
            description = "Author's biography or professional summary",
            example = "Experienced software developer with 10+ years in Java, Spring Boot, and microservices architecture. Passionate about clean code and mentoring junior developers.",
            maxLength = 1000
    )
    private String bio;

    // WEBSITE FIELD - Optional URL
    @Column(length = 255)
    @Pattern(
            regexp = "^(https?://)?(www\\.)?[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}(/.*)?$",
            message = "Website must be a valid URL"
    )
    @Size(max = 255, message = "Website URL cannot exceed 255 characters")
    @Schema(
            description = "Author's personal or professional website",
            example = "https://johndoe.dev",
            pattern = "^(https?://)?(www\\.)?[a-zA-Z0-9\\-\\.]+\\.[a-zA-Z]{2,}(/.*)?$"
    )
    private String website;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(description = "Timestamp when the author was created", example = "2024-01-15T10:30:00")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    @Schema(description = "Timestamp when the author was last updated", example = "2024-01-16T09:45:00")
    private LocalDateTime updatedAt;

    public Author(String name, String email){
        this.name = name;
        this.email = email;
    }

    public Author(String name, String email, String bio) {
        this.name = name;
        this.email = email;
        this.bio = bio;
    }

    public Author(String name, String email, String bio, String website) {
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.website = website;
    }

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public String getName(){ return name; }
    public void setName(String name){ this.name = name; }

    public String getEmail(){ return email; }
    public void setEmail(String email){ this.email = email; }

    public List<BlogPost> getPosts(){ return posts; }
    public void setPosts(List<BlogPost> posts){ this.posts = posts; }

    public String getBio(){ return bio; }
    public void setBio(String bio){ this.bio = bio; }

    public String getWebsite(){ return website; }
    public void setWebsite(String website){ this.website = website; }
}