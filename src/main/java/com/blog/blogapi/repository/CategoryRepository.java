package com.blog.blogapi.repository;

import com.blog.blogapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findById(List<Long> categoryIds);
}
