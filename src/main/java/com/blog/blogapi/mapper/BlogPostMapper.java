package com.blog.blogapi.mapper;

import com.blog.blogapi.DTO.BlogPostDTO;
import com.blog.blogapi.model.Author;
import com.blog.blogapi.model.BlogPost;
import com.blog.blogapi.model.Category;
import com.blog.blogapi.repository.AuthorRepository;
import com.blog.blogapi.repository.CategoryRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class BlogPostMapper {

    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public BlogPostMapper(AuthorRepository authorRepository, CategoryRepository categoryRepository) {
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    public BlogPost toEntity(BlogPostDTO dto) {
        BlogPost entity = new BlogPost();
        entity.setTitle(dto.getTitle());
        entity.setDate(dto.getDate());

        // Lookup Author by ID
        Long authorId = dto.getAuthor().getId();
        Optional<Author> authorOpt = authorRepository.findById(authorId);
        authorOpt.ifPresent(entity::setAuthor);

        // Lookup Categories by ID
        List<Category> categoryEntities = dto.getCategories().stream()
                .map(catDto -> categoryRepository.findById(catDto.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        entity.setCategories(categoryEntities);

        return entity;
    }
}
