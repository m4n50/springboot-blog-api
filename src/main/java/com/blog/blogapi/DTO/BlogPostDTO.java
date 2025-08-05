package com.blog.blogapi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class BlogPostDTO {

    @NotBlank
    private String title;

    @NotNull
    private Long authorId;

    private List<Long> categoryIds;

    public BlogPostDTO(){}

    public BlogPostDTO(String title, Long authorId, List<Long> categoryIds){
        this.title = title;
        this.authorId = authorId;
        this.categoryIds = categoryIds;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public List<Long> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Long> categoryIds) {
        this.categoryIds = categoryIds;
    }
}
