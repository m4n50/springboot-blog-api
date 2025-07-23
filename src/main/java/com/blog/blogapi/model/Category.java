package com.blog.blogapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
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

    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(!(o instanceof Category)) return false;
        Category category = (Category) o;
        return id != null && id.equals(category.getId());
    }

    @Override
    public int hashCode(){
        return Objects.hashCode(id);
    }
}

