package com.novarto.category.controllers;

import com.novarto.category.models.SingleProductDTO;
import com.novarto.category.services.interfaces.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Create a new product", response = SingleProductDTO.class, responseContainer = "Product")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful creation of a new product"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 400, message = "Bad Request")})
    @PostMapping("/")
    public @ResponseBody SingleProductDTO createProduct(@ApiParam(required = true, name = "product", value = "New product") @RequestBody SingleProductDTO productDTO) {
        try {
            log.info("Created product with name " + productDTO.getName());
            return productService.saveProduct(productDTO);
        } catch (ConstraintViolationException | IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @ApiOperation(value = "Update a specific product", response = SingleProductDTO.class, responseContainer = "Product")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful update of a given id product"),
            @ApiResponse(code = 404, message = "Product with given id does not exist, can't update it"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @PutMapping("/{id}")
    public @ResponseBody SingleProductDTO editProduct(@ApiParam(required = true, name = "product", value = "Product with the new properties") @RequestBody SingleProductDTO editDTO, @ApiParam(required = true, name = "id", value = "ID of the product you want to update") @PathVariable Long id) {
        try {
            log.info("Edited product with id " + id);
            SingleProductDTO originAL = this.getSingleProductDTO(id);
            return productService.editProduct(id, editDTO, originAL);
        } catch (ConstraintViolationException | IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    @ApiOperation(value = "Retrieve information about a specific product", response = SingleProductDTO.class, responseContainer = "Product")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of product", response = SingleProductDTO.class),
            @ApiResponse(code = 404, message = "Product with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping("/{id}")
    public @ResponseBody SingleProductDTO findProductById(@ApiParam(required = true, name = "id", value = "ID of the product you want to get") @PathVariable Long id) {
        log.info("Inside findProductById method in ProductController");
        return getSingleProductDTO(id);
    }

    @ApiOperation(value = "Delete a specific product")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful delete of product"),
            @ApiResponse(code = 404, message = "Product with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @DeleteMapping("/{id}")
    public void deleteProductById(@ApiParam(required = true, name = "id", value = "ID of the product you want to delete") @PathVariable Long id) {
        log.info("Inside deleteProductById method in ProductController");
        if (getSingleProductDTO(id) != null) {
            productService.deleteProductById(id);
        }
    }

    @ApiOperation(value = "Retrieve the full product list", response = SingleProductDTO.class, responseContainer = "List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of product list"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @GetMapping("/")
    public @ResponseBody List<SingleProductDTO> getAllProducts() {
        log.info("Inside getAllProducts method in ProductController");
        return productService.getAllProducts();
    }

    private SingleProductDTO getSingleProductDTO(Long id) {
        try {
            return productService.findProductById(id);
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
