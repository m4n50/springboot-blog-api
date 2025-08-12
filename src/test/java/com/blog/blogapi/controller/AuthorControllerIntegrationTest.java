package com.blog.blogapi.controller;

import com.blog.blogapi.model.Author;
import com.blog.blogapi.repository.AuthorRepository;
import com.blog.blogapi.repository.BlogPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        blogPostRepository.deleteAll();
        authorRepository.deleteAll();
        baseUrl = "http://localhost:" + port + "/api/authors";
    }

    @Test
    void testGetAllAuthors() {
        // Arrange
        Author author1 = new Author();
        author1.setName("John Doe");
        author1.setEmail("john@example.com");
        authorRepository.save(author1);

        Author author2 = new Author();
        author2.setName("Jane Doe");
        author2.setEmail("jane@example.com");
        authorRepository.save(author2);

        // Act
        ResponseEntity<Author[]> response = testRestTemplate.getForEntity(baseUrl, Author[].class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Author> authors = Arrays.asList(response.getBody());
        assertThat(authors).hasSize(2);
    }

    @Test
    void testAddAuthor() {
        // Arrange
        Author author = new Author();
        author.setName("New Author");
        author.setEmail("newauthor@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Author> request = new HttpEntity<>(author, headers);

        // Act
        ResponseEntity<String> response = testRestTemplate.postForEntity(baseUrl, request, String.class);

        // Debug: Print the response to see what's wrong
        System.out.println("Status: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}