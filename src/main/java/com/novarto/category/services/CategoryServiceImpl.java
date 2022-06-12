package com.novarto.category.services;

import com.novarto.category.entities.Category;
import com.novarto.category.mapper.NonNullFieldCopier;
import com.novarto.category.models.CategoryDTO;
import com.novarto.category.repositories.CategoryRepository;
import com.novarto.category.services.interfaces.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public CategoryDTO saveCategory(CategoryDTO categoryDto) {
        log.info("Inside saveCategory method in CategoryService");
        return this.mapper.map(categoryRepository.save(mapper.map(categoryDto, Category.class)), CategoryDTO.class);
    }

    @Override
    public CategoryDTO findCategoryById(Long id) {
        log.info("Inside findCategoryById method in the CategoryService");
        return this.mapper.map(categoryRepository.findById(id).orElseThrow(), CategoryDTO.class);

    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> list = categoryRepository.findAll();
        return list.stream().map(c -> this.mapper.map(c, CategoryDTO.class)).collect(Collectors.toList());
    }

    @Override
    public CategoryDTO editCategory(Long id, CategoryDTO editDTO, CategoryDTO originAL) throws IllegalAccessException {
        log.info("Inside updateCategory method in CategoryService");
        try {
            NonNullFieldCopier.copyNonNull(originAL, editDTO);
            Category output = categoryRepository.save(this.mapper.map(originAL, Category.class));
            return this.mapper.map(output, CategoryDTO.class);
        } catch (IllegalAccessException e) {
            throw new IllegalAccessException(e.getMessage());
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }

    @Override
    public void deleteCategoryById(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch (RuntimeException ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
