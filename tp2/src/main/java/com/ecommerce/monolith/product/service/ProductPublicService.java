package com.ecommerce.monolith.product.service;

import com.ecommerce.monolith.product.dto.ProductDTO;

public interface ProductPublicService {
    ProductDTO requireProduct(Long id);

    boolean existsById(Long id);
}
