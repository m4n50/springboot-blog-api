package com.blog.blogapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger/OpenAPI 3 configuration for the Blog API
 * Provides comprehensive API documentation and interactive testing interface
 */
@Configuration
public class SwaggerConfig {

    @Value("${server.port:8080}")
    private String serverPort;

    @Bean
    public OpenAPI blogApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Professional Blog API")
                        .description("""
                                A comprehensive REST API for managing a professional blog platform.
                                
                                **Features:**
                                • Complete CRUD operations for Authors, Categories, and Blog Posts
                                • Advanced search and filtering capabilities
                                • Pagination support for large datasets
                                • Professional error handling with detailed responses
                                • Validation and data integrity constraints
                                
                                **Built with:**
                                • Spring Boot 3.x
                                • Spring Data JPA
                                • PostgreSQL Database
                                • Docker for containerization
                                
                                **Perfect for:**
                                • Content Management Systems
                                • Blog platforms
                                • News websites
                                • Educational platforms
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Bruno Manso")
                                .email("bmansoweb@gmail.com")
                                .url("https://github.com/m4n50/springboot-blog-api"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:" + serverPort)
                                .description("Development Server"),
                        new Server()
                                .url("https://your-blog-api.herokuapp.com") // later
                                .description("Production Server (when deployed)")
                ));
    }
}