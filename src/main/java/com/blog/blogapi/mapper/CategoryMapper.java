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
}
