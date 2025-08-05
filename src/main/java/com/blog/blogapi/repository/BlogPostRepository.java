package com.blog.blogapi.repository;

import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Long> {
    List<BlogPost> findByTitleContainingIgnoreCase(String keyword);
    Page<BlogPost> findAll(Pageable page);
    List<BlogPost> findByCategoriesContaining(Category category);
    List<BlogPost> findByAuthorId(Long authorId);
}