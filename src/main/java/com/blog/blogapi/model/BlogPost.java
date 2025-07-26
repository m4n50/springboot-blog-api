package com.blog.blogapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
public class BlogPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonManagedReference
    private Author author;

    private LocalDate date;

    @ManyToMany
    @JoinTable(
            name = "blogpost_category",
            joinColumns = @JoinColumn(name = "blogpost_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @JsonIgnore
    private List<Category> categories;

    public BlogPost(){}

    public BlogPost(Long id, String title, String content, LocalDate date, Author author, List<Category> categories){
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.author = author;
        this.categories = categories;
    }

    public Long getId(){ return id; }
    public void setId(Long id){ this.id = id; }

    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }

    public String getContent(){ return content; }
    public void setContent(String content){ this.content = content; }

    public LocalDate getDate(){ return date; }
    public void setDate(LocalDate date){ this.date = date; }

    public Author getAuthor(){ return author; }
    public void setAuthor(Author author){ this.author = author; }

    public List<Category> getCategories(){ return categories; }
    public void setCategories(List<Category> categories){ this.categories = categories; }
}
