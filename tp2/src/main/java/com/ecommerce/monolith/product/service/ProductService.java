package com.ecommerce.monolith.product.service;
import java.util.List;

import com.ecommerce.monolith.product.dto.CreateProductRequest;
import com.ecommerce.monolith.product.dto.ProductDTO;
public interface ProductService {
    List<ProductDTO> getAllProducts();

    ProductDTO getProductById(Long id);

    ProductDTO createProduct(CreateProductRequest request);

    ProductDTO updateProduct(Long id, CreateProductRequest request);

    void deleteProduct(Long id);
}
