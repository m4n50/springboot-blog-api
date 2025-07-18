package com.blog.blogapi.repository;

import com.blog.blogapi.model.BlogPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPost, Integer> {
    List<BlogPost> findByTitleContainingIgnoreCase(String keyword);
}
