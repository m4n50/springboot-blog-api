package com.blog.blogapi.DTO;

import com.blog.blogapi.model.Author;
import com.blog.blogapi.model.Category;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class BlogPostDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "Author is required")
    @Valid
    private Author author;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotNull(message = "Categories are required")
    @Valid
    private List<@NotNull(message = "Each category must be provided") Category> categories;

    public BlogPostDTO(){}

    public BlogPostDTO(String title, Author author, LocalDate date, List<Category> categories){
        this.title = title;
        this.author = author;
        this.date = date;
        this.categories = categories;
    }

    public String getTitle(){
        return title;
    }

    public Author getAuthor(){
        return author;
    }

    public LocalDate getDate(){
        return date;
    }

    public List<Category> getCategories(){
        return categories;
    }
}
