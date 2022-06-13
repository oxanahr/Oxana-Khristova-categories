package com.novarto.category.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.novarto.category.controllers.ProductController;
import com.novarto.category.entities.Category;
import com.novarto.category.entities.Product;
import com.novarto.category.models.SingleProductDTO;
import com.novarto.category.repositories.ProductRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.mockito.BDDMockito.given;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@WebMvcTest(ProductController.class)
public class ProductService {
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private ProductRepository mockRepository;
    @MockBean
    private ProductServiceImpl productService;
    @Mock
    private ModelMapper modelMapper;

    SingleProductDTO firstSingleProductDTO = new SingleProductDTO();
    SingleProductDTO secondSingleProductDTO = new SingleProductDTO();
    SingleProductDTO thirdSingleProductDTO = new SingleProductDTO();
    List<SingleProductDTO> dtoList = new ArrayList<SingleProductDTO>();
    SingleProductDTO dtoOne = new SingleProductDTO();
    SingleProductDTO dtoTwo = new SingleProductDTO();
    SingleProductDTO dtoThree = new SingleProductDTO();

    @Before
    public void setUp() {
        this.modelMapper = new ModelMapper();
        this.productService = new ProductServiceImpl(this.mockRepository, this.modelMapper);

        firstSingleProductDTO.setProductId(222L);
        firstSingleProductDTO.setName("shirt");
        firstSingleProductDTO.setDescription("summer middle 100% cotton");
        firstSingleProductDTO.setPrice(39.88);
        firstSingleProductDTO.setCategoryId(3L);

        secondSingleProductDTO.setProductId(122L);
        secondSingleProductDTO.setName("milk");
        secondSingleProductDTO.setDescription("Serdika 3,6% 1L");
        secondSingleProductDTO.setPrice(3.88);
        secondSingleProductDTO.setCategoryId(1L);

        thirdSingleProductDTO.setProductId(156L);
        thirdSingleProductDTO.setName("orange");
        thirdSingleProductDTO.setDescription("GREECE");
        thirdSingleProductDTO.setPrice(1.89);
        thirdSingleProductDTO.setCategoryId(31L);

        dtoOne.setProductId(1L);
        dtoOne.setName("milk");
        dtoOne.setDescription("Vereya");
        dtoOne.setPrice(6.44);
        dtoOne.setCategoryId(null);

        dtoTwo.setProductId(2L);
        dtoTwo.setName("dress");
        dtoTwo.setDescription("100% cotton");
        dtoTwo.setPrice(8.88);
        dtoTwo.setCategoryId(null);

        dtoThree.setProductId(3L);
        dtoThree.setName("orange");
        dtoThree.setDescription("Country: Greece");
        dtoThree.setPrice(1.77);
        dtoThree.setCategoryId(null);

        dtoList.add(dtoOne);
        dtoList.add(dtoTwo);
        dtoList.add(dtoThree);
    }

    @Test
    public void createProduct() throws Exception {
        Product product = new Product(222L,"shirt", "summer middle 100% cotton", 39.88, new Category());
        when(mockRepository.save(isA(Product.class))).thenReturn(product);

        SingleProductDTO singleProductDTO = new SingleProductDTO();
        singleProductDTO.setName("shirt");
        singleProductDTO.setDescription("summer middle 100% cotton");
        singleProductDTO.setPrice(39.88);
        singleProductDTO.setProductId(222L);
        singleProductDTO.setCategoryId(72L);

        given(productService.saveProduct(singleProductDTO)).willReturn(singleProductDTO);

        this.mockMvc
                .perform(MockMvcRequestBuilders.post("/products/").content(asJsonString(singleProductDTO))
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void findProductById() throws Exception {
        // Arrange
        when(mockRepository.findById(222L))
                .thenReturn(Optional.of(new Product(222L, "shirt", "summer middle 100% cotton", 39.88, new Category())));

        // Act
        productService.findProductById(222L);

        // Assert
        verify(mockRepository, times(1)).findById(222L);
    }

    @Test
    public void getAllProducts() {
        List<Product> list = new ArrayList<Product>();
        Product prodOne = new Product(1L, "milk", "Vereya", 6.44, new Category());
        Product prodTwo = new Product(2L, "dress", "100% cotton", 8.88, new Category());
        Product prodThree = new Product(3L, "orange", "Country: Greece", 1.77, new Category());

        list.add(prodOne);
        list.add(prodTwo);
        list.add(prodThree);

        // Act
        when(mockRepository.findAll()).thenReturn(list);
        given(productService.getAllProducts()).willReturn(dtoList);

        //test
        assertEquals(list.size(), dtoList.size());
        verify(mockRepository, times(0)).findAll();
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
