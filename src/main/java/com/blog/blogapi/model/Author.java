package com.blog.blogapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;

    @OneToMany(mappedBy = "author")
    @JsonBackReference
    private List<BlogPost> posts;

    public Author(){}

    public Author(String name, String email){
        this.name = name;
        this.email = email;
    }

    public int getId(){ return id; }

    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }

    public String getEmail(){ return email; }

    public void setEmail(String email){ this.email = email; }

    public List<BlogPost> getPosts(){ return posts; }

    public void setPosts(List<BlogPost> posts){ this.posts = posts; }
}
