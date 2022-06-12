package com.novarto.category.controllers;

import com.novarto.category.models.CategoryDTO;
import com.novarto.category.services.interfaces.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/categories")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ApiOperation(value = "Create a new category", response = CategoryDTO.class, responseContainer = "Category")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful creation of a new category"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("/")
    public @ResponseBody CategoryDTO createCategory(@ApiParam(required = true, name = "category", value = "New category") @RequestBody CategoryDTO categoryDto) {
        try {
            log.info("Created category with name " + categoryDto.getName());
            return categoryService.saveCategory(categoryDto);
        } catch (ConstraintViolationException | IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @ApiOperation(value = "Retrieve information about a specific category")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of category", response = CategoryDTO.class),
            @ApiResponse(code = 404, message = "Category with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping("/{id}")
    public @ResponseBody CategoryDTO findCategoryById(@ApiParam(required = true, name = "id", value = "ID of the category you want to get") @PathVariable("id") Long id) {
        log.info("Inside findCategoryById method in CategoryController");
        return this.getCategory(id);
    }


    @ApiOperation(value = "Retrieve the full category list", response = CategoryDTO.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of category list"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping("/")
    public @ResponseBody List<CategoryDTO> getAllCategories() {
        log.info("Inside getAllCategories method in CategoryController");
        return categoryService.getAllCategories();
    }


    @ApiOperation(value = "Delete a specific category")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful delete of category"),
            @ApiResponse(code = 404, message = "Category with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @DeleteMapping("/{id}")
    public void deleteCategoryById(@ApiParam(required = true, name = "id", value = "ID of the category you want to delete") @PathVariable Long id) {
        log.info("Inside deleteCategoryById method in CategoryController");
        if (this.getCategory(id) != null) {
            categoryService.deleteCategoryById(id);
        }
    }


    @ApiOperation(value = "Update a specific category")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful update of a given id category"),
            @ApiResponse(code = 404, message = "Category with given id does not exist, can't update it"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PutMapping("/{id}")
    public @ResponseBody CategoryDTO editCategory(@ApiParam(required = true, name = "id", value = "ID of the category you want to update") @PathVariable("id") Long id,
                                                  @ApiParam(required = true, name = "category", value = "Category with the new properties") @RequestBody CategoryDTO editDTO) {
        log.info("Inside editCategory method in CategoryController");
        try {
            CategoryDTO originAL = this.getCategory(id);
            log.info("Edited category with id " + id);
            return categoryService.editCategory(id, editDTO, originAL);
        } catch (ConstraintViolationException | IllegalArgumentException | IllegalAccessException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    private CategoryDTO getCategory(Long id) {
        try {
            return categoryService.findCategoryById(id);
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
