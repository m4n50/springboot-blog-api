package com.blog.blogapi.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String content;
    private LocalDate date;
    private String author;
    private List<String> categories;

    public BlogPost(){}

    public BlogPost(int id, String title, String content, LocalDate date, String author, List<String> categories){
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.author = author;
        this.categories = categories;
    }

    public int getId(){ return id; }
    public void setId(int id){ this.id = id; }

    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }

    public String getContent(){ return content; }
    public void setContent(String content){ this.content = content; }

    public LocalDate getDate(){ return date; }
    public void setDate(LocalDate date){ this.date = date; }

    public String getAuthor(){ return author; }
    public void setAuthor(String author){ this.author = author; }

    public List<String> getCategories(){ return categories; }
    public void setCategories(List<String> categories){ this.categories = categories; }
}
