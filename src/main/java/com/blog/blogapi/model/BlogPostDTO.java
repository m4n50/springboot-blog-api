package com.blog.blogapi.model;

import java.time.LocalDate;
import java.util.List;

public class BlogPostDTO {
    private String title;
    private Author author;
    private LocalDate date;
    private List<Category> categories;

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
