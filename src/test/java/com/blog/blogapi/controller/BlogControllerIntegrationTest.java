package com.blog.blogapi.controller;

import com.blog.blogapi.model.Category;
import com.blog.blogapi.repository.BlogPostRepository;
import com.blog.blogapi.repository.CategoryRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BlogControllerIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    public void setUp(){
        RestAssured.port = port;
        categoryRepository.deleteAll();
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
