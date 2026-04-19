package com.jfb.jottasburger.product.service;

import com.jfb.jottasburger.category.model.Category;
import com.jfb.jottasburger.category.repository.CategoryRepository;
import com.jfb.jottasburger.exception.BusinessException;
import com.jfb.jottasburger.exception.ResourceNotFoundException;
import com.jfb.jottasburger.product.dto.CreateProductRequest;
import com.jfb.jottasburger.product.dto.ProductResponse;
import com.jfb.jottasburger.product.dto.UpdateProductRequest;
import com.jfb.jottasburger.product.model.Product;
import com.jfb.jottasburger.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductResponse create(CreateProductRequest request) {
        Category category = findActiveCategoryById(request.categoryId());

        Product product = new Product(
                normalizeText(request.name()),
                normalizeText(request.description()),
                request.price(),
                category
        );

        Product savedProduct = productRepository.save(product);
        return toResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAllActive() {
        return productRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll() {
        return productRepository.findAllByOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = findProductById(id);
        return toResponse(product);
    }

    @Transactional
    public ProductResponse update(Long id, UpdateProductRequest request) {
        Product product = findProductById(id);
        Category category = findActiveCategoryById(request.categoryId());

        product.updateDetails(
                normalizeText(request.name()),
                normalizeText(request.description()),
                request.price(),
                category
        );

        return toResponse(product);
    }

    @Transactional
    public void activate(Long id) {
        Product product = findProductById(id);
        product.activate();
    }

    @Transactional
    public void deactivate(Long id) {
        Product product = findProductById(id);
        product.deactivate();
    }

    private Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    private Category findActiveCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));

        if (!category.isActive()) {
            throw new BusinessException("Inactive category cannot be associated with a product");
        }

        return category;
    }

    private String normalizeText(String value) {
        return value.trim();
    }

    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.isActive(),
                product.getCategory().getId(),
                product.getCategory().getName(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}