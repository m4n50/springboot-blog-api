package com.blog.blogapi.controller;

import com.blog.blogapi.model.Author;
import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.model.Category;
import com.blog.blogapi.repository.AuthorRepository;
import com.blog.blogapi.repository.BlogPostRepository;
import com.blog.blogapi.repository.CategoryRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class BlogControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp(){
        RestAssured.port = port;
        blogPostRepository.deleteAll();
        authorRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void testCreateBlogPost(){

        Author author = new Author("Jane Doe", "jane@example.com");
        author = authorRepository.save(author);

        Category category = new Category("Tech", "Technology related");
        category = categoryRepository.save(category);

        String json = """
        {
          "title": "RestAssured Post",
          "date": "%s",
          "authorId": %d,
          "categoryIds": [%d]
        }
        """.formatted(LocalDate.now(), author.getId(), category.getId());

        given()
                .contentType("application/json")
                .body(json)
        .when()
                .post("api/posts")
        .then()
                .statusCode(201);

        List<BlogPost> posts = blogPostRepository.findAll();
        assertThat(posts).hasSize(1);
        assertThat(posts.get(0).getTitle()).isEqualTo("RestAssured Post");
    }

    @Test
    public void testCreateBlogPost_InvalidData_ShouldReturn400_AndValidationMessages() {
        // Missing title and invalid categoryIds (empty list)
        String invalidJson = """
    {
      "title": "",
      "date": "%s",
      "authorId": null,
      "categoryIds": []
    }
    """.formatted(LocalDate.now());

        var response = given()
                .contentType("application/json")
                .body(invalidJson)
            .when()
                .post("/api/posts")
            .then()
                .statusCode(400)
                .contentType("application/json")
                .extract()
                .response();

        System.out.println("Validation response:");
        System.out.println(response.prettyPrint());

        List<String> messages = response.jsonPath().getList("errors.message");

        assertThat(messages).contains(
                "Title is required",
                "Author ID is required",
                "At least one category ID must be provided"
        );
    }

    @Test
    void testH2Connection() {
        System.out.println("Running with test DB (H2)");
    }

   @Test
   public void testGetAllPosts(){

        var response = RestAssured.get("/api/posts")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .extract()
                .as(List.class);

        assertThat(response).isNotNull();
    }
}
