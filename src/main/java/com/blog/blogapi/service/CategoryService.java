package com.blog.blogapi.service;

import com.blog.blogapi.DTO.CategoryDTO;
import com.blog.blogapi.mapper.CategoryMapper;
import com.blog.blogapi.model.Category;
import com.blog.blogapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    /**
     * Search categories by name containing keyword (case-insensitive)
     * Returns categories as DTOs
     */
    @Transactional(readOnly = true)
    public List<CategoryDTO> searchCategoriesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Search name cannot be null or empty");
        }

        // Find categories containing the name (case-insensitive)
        List<Category> categories = categoryRepository.findByNameContainingIgnoreCase(name.trim());

        // Convert to DTOs using your mapper
        return categories.stream()
                .map(categoryMapper::toDTO) // Assumes you have a toDTO method
                .toList();
    }

    /**
     * Check if a category with the given name already exists (case-insensitive)
     * Useful for preventing duplicate category names
     */
    @Transactional(readOnly = true)
    public boolean categoryExistsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false; // Consider empty/null names as non-existent
        }

        // Check if category exists by name (case-insensitive)
        return categoryRepository.existsByNameIgnoreCase(name.trim());
    }

    /**
     * Get all categories with their post counts
     * Useful for admin dashboards or category listings with statistics
     */
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getCategoriesWithPostCounts() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> {
                    Map<String, Object> categoryWithCount = new HashMap<>();
                    categoryWithCount.put("id", category.getId());
                    categoryWithCount.put("name", category.getName());
                    categoryWithCount.put("description", category.getDescription());

                    // Count posts in this category
                    long postCount = category.getPosts() != null ? category.getPosts().size() : 0;
                    categoryWithCount.put("postCount", postCount);

                    return categoryWithCount;
                })
                .toList();
    }

    /**
     * Get total count of all categories
     * Useful for pagination and statistics
     */
    @Transactional(readOnly = true)
    public long getCategoriesCount() {
        return categoryRepository.count(); // Built-in JPA method
    }
}
