package com.blog.blogapi.service;

import com.blog.blogapi.DTO.CategoryDTO;
import com.blog.blogapi.mapper.CategoryMapper;
import com.blog.blogapi.model.Category;
import com.blog.blogapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public CategoryDTO addCategory(CategoryDTO dto) {
        Category category = categoryMapper.dtoToEntity(dto);
        Category saved = categoryRepository.save(category);
        return categoryMapper.entityToDto(saved);
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return categoryMapper.entityToDto(category);
    }

    public CategoryDTO updateCategory(Long id, CategoryDTO dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());

        Category updated = categoryRepository.save(existing);
        return categoryMapper.entityToDto(updated);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }
}
