package com.blog.blogapi.repository;

import com.blog.blogapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByIdIn(List<Long> categoryIds);
    List<Category> findByNameContainingIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.posts")
    List<Category> findAllWithPosts();
}
