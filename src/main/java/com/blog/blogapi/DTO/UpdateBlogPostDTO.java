package com.blog.blogapi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateBlogPostDTO {

    @NotBlank(message = "Title is required")
    private String title;

    private String content;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Author ID is required")
    private Long authorId;

    private List<Long> categoryIds;

    public UpdateBlogPostDTO(){}

    public UpdateBlogPostDTO(String title, String content, LocalDate date, Long authorId, List<Long> categoryIds){
        this.title = title;
        this.content = content;
        this.date = date;
        this.authorId = authorId;
        this.categoryIds = categoryIds;
    }

    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }

    public String getContent(){ return content; }
    public void setContent(String title){ this.content = content; }

    public LocalDate getDate(){ return date; }
    public void setTDate(LocalDate title){ this.date = date; }

    public Long getAuthorId(){ return authorId; }
    public void setAuthorId(Long authorId){ this.authorId = authorId; }

    public List<Long> getCategoryIds(){ return categoryIds; }
    public void setCategoryIds(List<Long> categoryIds){ this.categoryIds = getCategoryIds(); }
}
