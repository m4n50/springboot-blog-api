package com.blog.blogapi.mapper;

import com.blog.blogapi.DTO.BlogPostDTO;
import com.blog.blogapi.DTO.UpdateBlogPostDTO;
import com.blog.blogapi.exception.ResourceNotFoundException;
import com.blog.blogapi.model.Author;
import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.model.Category;
import com.blog.blogapi.repository.CategoryRepository;
import com.blog.blogapi.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BlogPostMapper {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private CategoryRepository categoryRepository;

    public BlogPost dtoToEntity(BlogPostDTO dto) {
        BlogPost entity = new BlogPost();
        entity.setTitle(dto.getTitle());
        entity.setDate(dto.getDate());

        Author author = authorService.getAuthorById(dto.getAuthorId());
        entity.setAuthor(author);

        List<Category> categories = dto.getCategoryIds().stream()
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id)))
                        .collect(Collectors.toList());
        entity.setCategories(categories);

        return entity;
    }

    public BlogPostDTO entityToDto(BlogPost post) {
        BlogPostDTO dto = new BlogPostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setDate(post.getDate());
        dto.setAuthorId(post.getAuthor().getId());

        List<Long> categoryIds = post.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList());
        dto.setCategoryIds(categoryIds);

        return dto;
    }

    public static BlogPostDTO toDTO(BlogPost post) {
        BlogPostDTO dto = new BlogPostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setDate(post.getDate());
        // map other fields
        return dto;
    }

    public void updateEntityFromDTO(UpdateBlogPostDTO dto, BlogPost post){
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setDate(dto.getDate());

        Author author = authorService.getAuthorById(dto.getAuthorId());
        post.setAuthor(author);

        if(dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()){
            List<Category> categories = dto.getCategoryIds().stream()
                .map(id -> categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Category not found with id " + id)))
                .collect(Collectors.toList());
            post.setCategories(categories);
        }
    }
}
