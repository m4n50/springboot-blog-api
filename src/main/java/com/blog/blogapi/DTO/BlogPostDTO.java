package com.blog.blogapi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class BlogPostDTO {

    private Long id;

    @NotNull
    private Long authorId;

    @NotBlank
    private String title;

    private LocalDate date;

    private String content;

    private List<Long> categoryIds;

    public BlogPostDTO(){}

    public BlogPostDTO(Long authorId, String title,LocalDate date, String content, List<Long> categoryIds){
        this.authorId = authorId;
        this.title = title;
        this.date = date;
        this.content = content;
        this.categoryIds = categoryIds;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public List<Long> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(List<Long> categoryIds) { this.categoryIds = categoryIds; }
}
