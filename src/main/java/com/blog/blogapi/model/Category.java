package com.blog.blogapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Category name is required")
    private String name;

    @Size(max = 255, message = "Description must be under 255 characters")
    private String description;

    @ManyToMany(mappedBy = "categories")
    @JsonIgnore
    private List<BlogPost> posts;

    public Category(){}

    public Category(Long id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId(){ return id; }

    public void setId(Long categoryId) { this.id = categoryId; }

    public String getName(){ return name; }

    public void setName(String name){ this.name = name; }

    public String getDescription(){ return description; }

    public void setDescription(String description){ this.description = description; }

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

