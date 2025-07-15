package com.blog.blogapi.model;

import java.time.LocalDate;
import java.util.List;

public class BlogPostDTO {
    private String title;
    private String author;
    private LocalDate date;
    private List<String> categories;

    public BlogPostDTO(){}

    public BlogPostDTO(String title, String author, LocalDate date, List<String> categories){
        this.title = title;
        this.author = title;
        this.date = date;
        this.categories = categories;
    }

    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public LocalDate getDate(){
        return date;
    }

    public List<String> getCategories(){
        return categories;
    }
}
