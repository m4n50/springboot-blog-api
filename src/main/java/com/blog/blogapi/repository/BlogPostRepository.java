package com.blog.blogapi.repository;

import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByTitleContainingIgnoreCase(String keyword);
    List<BlogPost> findByCategoriesContaining(Category category);
    List<BlogPost> findByAuthorId(Long authorId);
    @Query("SELECT COUNT(DISTINCT bp) FROM BlogPost bp JOIN bp.categories c WHERE c.id = :categoryId")
    long countByCategoryId(@Param("categoryId") Long categoryId);
}