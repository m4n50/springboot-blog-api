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

        Author author = new Author();
        author.setName("Jane Doe");

        Category category = new Category();
        category.setName("Tech");
        category.setDescription("Technology related");

        String json = """
        {
           "title": "RestAssured Post",
           "date": "%s",
           "author": {
             "name": "Jane Doe",
             "email": "jane@example.com"
           },
           "categories": [
             {
               "name": "Tech",
               "description": "Technology related"
             }
           ]
        }
        """.formatted(LocalDate.now(), author.getId(), category.getId());

        given()
                .contentType("application/json")
                .body(json)
        .when()
                .post("api/posts")
        .then()
                .statusCode(200);

        List<BlogPost> posts = blogPostRepository.findAll();
        assert(posts.size() == 1);
        assert(posts.get(0).getTitle().equals("RestAssured Post"));
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
