package com.novarto.category.services;
import com.novarto.category.entities.Category;
import com.novarto.category.repositories.CategoryRepository;
import com.novarto.category.repositories.ProductRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.Silent.class)
public class CategoryService {
    @Mock
    private CategoryRepository mockRepository;

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;
    private ModelMapper modelMapper;

    @Before
    public void setUp() {
        this.modelMapper = new ModelMapper();
        this.categoryService = new CategoryServiceImpl(this.mockRepository, this.modelMapper);
    }

    @Test
    public void findCategoryById() {
        // Arrange
        Mockito.when(mockRepository.findById(5L))
                .thenReturn(Optional.of(new Category(5L, "Food", new ArrayList<>())));

        // Act
        categoryService.findCategoryById(5L);

        // Assert
        Mockito.verify(mockRepository, Mockito.times(1)).findById(5L);
    }

}
