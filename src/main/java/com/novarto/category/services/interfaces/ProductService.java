package com.novarto.category.services.interfaces;


import com.novarto.category.models.SingleProductDTO;

import java.util.List;


public interface ProductService {

    SingleProductDTO saveProduct(SingleProductDTO productDTO);

    SingleProductDTO findProductById(Long id);

    void deleteProductById(Long id);

    SingleProductDTO editProduct(Long id, SingleProductDTO editDTO, SingleProductDTO originalDTO);

    List<SingleProductDTO> getAllProducts();
}
