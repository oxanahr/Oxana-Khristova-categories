package com.novarto.category.services.interfaces;

import com.novarto.category.models.CategoryDTO;

import java.util.List;


public interface CategoryService {

    CategoryDTO saveCategory(CategoryDTO categoryDto);

    CategoryDTO findCategoryById(Long id);

    void deleteCategoryById(Long id);

    List<CategoryDTO> getAllCategories();

    CategoryDTO editCategory(Long id, CategoryDTO categoryDto, CategoryDTO originAL) throws IllegalAccessException;
}
