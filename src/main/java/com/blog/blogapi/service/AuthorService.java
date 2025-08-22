package com.blog.blogapi.service;

import com.blog.blogapi.exception.ResourceNotFoundException;
import com.blog.blogapi.model.Author;
import com.blog.blogapi.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true) // Default: read-only transactions
@RequiredArgsConstructor // Modernized dependency injection
public class AuthorService {

    private final AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Optional<Author> getAuthorByIdOptional(Long id) {
        return authorRepository.findById(id);
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
    }

    @Transactional
    public Author createAuthor(Author author) {
        if (author.getName() != null) {
            author.setName(author.getName().trim()); // Fixed: was missing setName
        }

        if (author.getEmail() != null) {
            author.setEmail(author.getEmail().trim().toLowerCase());
        }

        if (existsByEmail(author.getEmail())) {
            throw new IllegalArgumentException("Author with email " + author.getEmail() + " already exists");
        }

        return authorRepository.save(author);
    }

    @Transactional
    public Author updateAuthor(Long id, Author updatedAuthor) {
        return authorRepository.findById(id)
                .map(existingAuthor -> {
                    // Only update non-null fields (partial update)
                    if (updatedAuthor.getName() != null && !updatedAuthor.getName().trim().isEmpty()) {
                        existingAuthor.setName(updatedAuthor.getName().trim());
                    }

                    if (updatedAuthor.getEmail() != null && !updatedAuthor.getEmail().trim().isEmpty()) {
                        String newEmail = updatedAuthor.getEmail().trim().toLowerCase();

                        // Business rule: check if new email conflicts with another author
                        if (!existingAuthor.getEmail().equals(newEmail) && existsByEmail(newEmail)) {
                            throw new IllegalArgumentException("Email " + newEmail + " is already taken by another author");
                        }

                        existingAuthor.setEmail(newEmail);
                    }

                    // Update other fields if they exist
                    if (updatedAuthor.getBio() != null) {
                        existingAuthor.setBio(updatedAuthor.getBio().trim());
                    }

                    if (updatedAuthor.getWebsite() != null) {
                        existingAuthor.setWebsite(updatedAuthor.getWebsite().trim());
                    }

                    return authorRepository.save(existingAuthor);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));
    }

    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id " + id);
        }

        // Business rule: In the future, you might want to:
        // - Check if author has published posts
        // - Maybe soft delete instead of hard delete
        // - Transfer posts to another author

        authorRepository.deleteById(id);
    }

    public boolean existsByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // For now, we'll implement this manually
        // Later you can add this method to the repository: existsByEmailIgnoreCase(String email)
        return authorRepository.findAll().stream()
                .anyMatch(author -> author.getEmail().equalsIgnoreCase(email.trim()));
    }

    public List<Author> findAuthorsByNameContaining(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return getAllAuthors();
        }

        return authorRepository.findAll().stream()
                .filter(author -> author.getName().toLowerCase()
                        .contains(searchTerm.toLowerCase()))
                .toList();
    }

    /**
     * Get total count of authors
     * Useful for dashboard/admin pages
     */
    public long getTotalAuthorCount() {
        return authorRepository.count();
    }

    // =====================================================
    // SWAGGER-COMPATIBLE METHODS (Required by Controller)
    // =====================================================

    /**
     * Get authors count - Required by Swagger-documented endpoint
     */
    public long getAuthorsCount() {
        return authorRepository.count();
    }

    /**
     * Search authors by name - Required by Swagger-documented endpoint
     */
    public List<Author> searchAuthorsByName(String name) {
        return findAuthorsByNameContaining(name);
    }

    /**
     * Check if author exists by email - Required by Swagger-documented endpoint
     */
    public boolean authorExistsByEmail(String email) {
        return existsByEmail(email);
    }

    /**
     * Get authors with post counts - Required by Swagger-documented endpoint
     * This method returns authors along with their blog post counts
     */
    public List<Map<String, Object>> getAuthorsWithPostCounts() {
        List<Author> authors = getAllAuthors();

        return authors.stream()
                .map(author -> {
                    Map<String, Object> authorWithCount = new HashMap<>();
                    authorWithCount.put("id", author.getId());
                    authorWithCount.put("name", author.getName());
                    authorWithCount.put("email", author.getEmail());
                    authorWithCount.put("bio", author.getBio());
                    authorWithCount.put("website", author.getWebsite());

                    // Count posts for this author
                    // You'll need to add this to your BlogService or create a method here
                    // For now, returning 0 as placeholder
                    long postCount = getPostCountByAuthor(author.getId());
                    authorWithCount.put("postCount", postCount);

                    return authorWithCount;
                })
                .toList();
    }

    /**
     * Helper method to get post count by author
     * Uses repository query to count posts efficiently
     */
    private long getPostCountByAuthor(Long authorId) {
        return authorRepository.countPostsByAuthorId(authorId);
    }
}