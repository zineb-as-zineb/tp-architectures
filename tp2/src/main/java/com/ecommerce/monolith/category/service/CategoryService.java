package com.ecommerce.monolith.category.service;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.monolith.category.model.Category;
import com.ecommerce.monolith.category.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository repository;
    @Transactional(readOnly = true)
    public List<Category> getAll() {
        return repository.findAll();
    }
    @Transactional(readOnly = true)
    public Category getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + id));
    }
    public Category create(Category category) {
        return repository.save(category);
    }
    public Category update(Long id, Category details) {
        Category category = getById(id);
        category.setName(details.getName());
        return category;
    }
    public void delete(Long id) {
        Category category = getById(id);
        repository.delete(category);
    }
}
