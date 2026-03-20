package com.ecommerce.monolith.product.service;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.monolith.category.model.Category;
import com.ecommerce.monolith.category.repository.CategoryRepository;
import com.ecommerce.monolith.product.dto.CreateProductRequest;
import com.ecommerce.monolith.product.dto.ProductDTO;
import com.ecommerce.monolith.product.mapper.ProductMapper;
import com.ecommerce.monolith.product.model.Product;
import com.ecommerce.monolith.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService, ProductPublicService {
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;
    @Override
    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return mapper.toDTOList(repository.findAll());
    }
    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        return mapper.toDTO(findProduct(id));
    }
    @Override
    public ProductDTO createProduct(CreateProductRequest request) {
        Product product = mapper.toEntity(request);
        product.setCategory(findCategory(request.getCategoryId()));
        Product saved = repository.save(product);
        return mapper.toDTO(saved);
    }
    @Override
    public ProductDTO updateProduct(Long id, CreateProductRequest request) {
        Product product = findProduct(id);
        mapper.updateEntity(request, product);
        product.setCategory(findCategory(request.getCategoryId()));
        Product updated = repository.save(product);
        return mapper.toDTO(updated);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Product not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO requireProduct(Long id) {
        return getProductById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    private Product findProduct(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }

    private Category findCategory(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));
    }
}
