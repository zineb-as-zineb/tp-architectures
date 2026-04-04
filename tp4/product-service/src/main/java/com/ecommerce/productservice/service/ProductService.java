package com.ecommerce.productservice.service;
import com.ecommerce.productservice.model.Product;
import com.ecommerce.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List; import java.util.Optional;
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    public List<Product> getAllProducts() {         return productRepository.findAll();     }
    public Optional<Product> getProductById(Long id) {         return productRepository.findById(id);     }
    public Product createProduct(Product product) {         return productRepository.save(product);     }
    public void deleteProduct(Long id) {         productRepository.deleteById(id);     }

    public Optional<Product> updateStock(Long id, Integer quantity) {
        return productRepository.findById(id).map(product -> {
            if (product.getStock() < quantity) {
                throw new RuntimeException("Stock insuffisant");
            }
            product.setStock(product.getStock() - quantity);
            return productRepository.save(product);
        });
    }
}