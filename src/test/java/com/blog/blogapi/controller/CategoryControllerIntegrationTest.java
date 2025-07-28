package com.blog.blogapi.controller;

import com.blog.blogapi.model.Category;
import com.blog.blogapi.repository.BlogPostRepository;
import com.blog.blogapi.repository.CategoryRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class CategoryControllerIntegrationTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BlogPostRepository blogPostRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp(){
        RestAssured.port = port;
    }

    @Test
    public void testAddAndGetCategory(){
        //Add a new category
        Category newCategory = new Category();
        newCategory.setName("Test Category");
        newCategory.setDescription("For integration testing");

        Category created = RestAssured.given()
                .contentType("application/json")
                .body(newCategory)
                .when()
                .post("/api/categories")
                .then()
                .statusCode(200)
                .extract()
                .as(Category.class);

        System.out.println("Created category ID: " + created.getId());

        assertThat(created).isNotNull();
        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("Test Category");

        Category fetched = RestAssured.get("/api/categories/{id}", created.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(Category.class);

        assertThat(fetched).isNotNull();
        assertThat(fetched.getId()).isEqualTo(created.getId());
        assertThat(fetched.getName()).isEqualTo("Test Category");
    }

    @Test
    public void testGetAllCategories(){
        Category cat1 = new Category(null,"Cat1","Desc1");
        Category cat2 = new Category(null,"Cat2","Desc2");
        categoryRepository.saveAll(List.of(cat1, cat2));

        List<Category> response = RestAssured.get("/api/categories")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", Category.class);

        assertThat(response).hasSize(2);
    }

    @Test
    public void testUpdateCategory(){
        Category cat = new Category(null, "Old name", "Old desc");
        cat = categoryRepository.save(cat);

        cat.setName("New Name");
        cat.setDescription("New Desc");

        Category updated = RestAssured.given()
                .contentType("application/json")
                .body(cat)
                .put("/api/categories/{id}", cat.getId())
                .then()
                .statusCode(200)
                .extract()
                .as(Category.class);

        assertThat(updated.getName()).isEqualTo("New Name");
        assertThat(updated.getDescription()).isEqualTo("New Desc");
    }

    @Test
    public void testDeleteCategory(){
        Category cat = new Category(null, "To Delete", "To Delete Desc");
        cat = categoryRepository.save(cat);

        RestAssured.delete("/api/categories/{id}", cat.getId())
                .then()
                .statusCode(204);

        boolean exists = categoryRepository.existsById(cat.getId());
        assertThat(exists).isFalse();
    }
}
