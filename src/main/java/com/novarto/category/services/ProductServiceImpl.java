package com.novarto.category.services;

import com.novarto.category.entities.Category;
import com.novarto.category.entities.Product;
import com.novarto.category.mapper.NonNullFieldCopier;
import com.novarto.category.models.SingleProductDTO;
import com.novarto.category.repositories.CategoryRepository;
import com.novarto.category.repositories.ProductRepository;
import com.novarto.category.services.interfaces.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper mapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @Override
    public SingleProductDTO saveProduct(SingleProductDTO productDTO) {
        return this.mapper.map(productRepository.save(mapper.map(productDTO, Product.class)), SingleProductDTO.class);
    }

    @Override
    public SingleProductDTO findProductById(Long id) {
        return this.mapper.map(productRepository.findById(id).orElseThrow(), SingleProductDTO.class);
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public SingleProductDTO editProduct(Long id, SingleProductDTO editDTO, SingleProductDTO originalDTO) {
        try {
            NonNullFieldCopier.copyNonNull(originalDTO, editDTO);
            Product output = productRepository.save(this.mapper.map(originalDTO, Product.class));
            return this.mapper.map(output, SingleProductDTO.class);
        } catch (NoSuchElementException ex) {
            throw new NoSuchElementException(ex);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SingleProductDTO> getAllProducts() {
        List<Product> list = productRepository.findAll();
        return list.stream().map(c -> this.mapper.map(c, SingleProductDTO.class)).collect(Collectors.toList());
    }
}
