package com.blog.blogapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Author name is required")
    private String name;

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @OneToMany(mappedBy = "author")
    @JsonBackReference
    private List<BlogPost> posts;

    public Author(){}

    public Author(String name, String email, Long id){
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public Long getId(){ return id; }

    public void setId(Long authorId){ this.id = id; }

    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }

    public String getEmail(){ return email; }

    public void setEmail(String email){ this.email = email; }

    public List<BlogPost> getPosts(){ return posts; }

    public void setPosts(List<BlogPost> posts){ this.posts = posts; }
}
