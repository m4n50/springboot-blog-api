package com.blog.blogapi.mapper;

import com.blog.blogapi.DTO.CategoryDTO;
import com.blog.blogapi.model.Category;
import org.springframework.stereotype.Component;


@Component
public class CategoryMapper {

    public Category dtoToEntity(CategoryDTO dto){
        return new Category(
                dto.getId(),
                dto.getName(),
                dto.getDescription()
        );
    }


    public CategoryDTO entityToDto(Category category){
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    public CategoryDTO toDTO(Category category) {  // Return CategoryDTO, not Object
        if (category == null) {
            return null; // Handle null input gracefully
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        dto.setDescription(category.getDescription());

        // Note: We don't map the 'posts' field because:
        // 1. It has @JsonIgnore annotation
        // 2. DTOs should be lightweight for API responses
        // 3. Avoid circular reference issues (BlogPost -> Category -> BlogPost...)

        return dto;
    }
}
