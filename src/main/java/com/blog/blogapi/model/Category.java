package com.blog.blogapi.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<BlogPost> posts;

    public Category(){}

    public Category(String name){
        this.name = name;
    }

    public int getId(){ return id; }

    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }

    public List<BlogPost> getPosts(){ return posts; }

    public void setPosts(List<BlogPost> posts){ this.posts = posts; }
}
