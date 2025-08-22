package com.blog.blogapi.repository;

import com.blog.blogapi.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    /**
     * Count blog posts by author ID
     * This method counts how many blog posts are associated with a specific author
     */
    @Query("SELECT COUNT(p) FROM BlogPost p WHERE p.author.id = :authorId")
    long countPostsByAuthorId(@Param("authorId") Long authorId);

    /**
     * Check if author exists by email (case-insensitive)
     * Useful for validation during author creation/update
     */
    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Author a WHERE LOWER(a.email) = LOWER(:email)")
    boolean existsByEmailIgnoreCase(@Param("email") String email);

    /**
     * Find authors by name containing keyword (case-insensitive)
     * Useful for search functionality
     */
    @Query("SELECT a FROM Author a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    java.util.List<Author> findByNameContainingIgnoreCase(@Param("name") String name);
}
