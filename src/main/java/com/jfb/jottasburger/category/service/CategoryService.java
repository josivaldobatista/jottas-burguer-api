package com.jfb.jottasburger.category.service;

import com.jfb.jottasburger.category.dto.CategoryResponse;
import com.jfb.jottasburger.category.dto.CreateCategoryRequest;
import com.jfb.jottasburger.category.dto.UpdateCategoryRequest;
import com.jfb.jottasburger.category.model.Category;
import com.jfb.jottasburger.category.repository.CategoryRepository;
import com.jfb.jottasburger.exception.BusinessException;
import com.jfb.jottasburger.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse create(CreateCategoryRequest request) {
        validateCategoryNameUniqueness(request.name());

        Category category = new Category(normalizeName(request.name()));
        Category savedCategory = categoryRepository.save(category);

        return toResponse(savedCategory);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponse> findAllActive() {
        return categoryRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoryResponse findById(Long id) {
        Category category = findCategoryById(id);
        return toResponse(category);
    }

    @Transactional
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {
        Category category = findCategoryById(id);
        String normalizedName = normalizeName(request.name());

        if (!category.getName().equalsIgnoreCase(normalizedName)
                && categoryRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new BusinessException("Category name already exists");
        }

        category.updateName(normalizedName);

        return toResponse(category);
    }

    @Transactional
    public void activate(Long id) {
        Category category = findCategoryById(id);
        category.activate();
    }

    @Transactional
    public void deactivate(Long id) {
        Category category = findCategoryById(id);
        category.deactivate();
    }

    private Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    private void validateCategoryNameUniqueness(String name) {
        if (categoryRepository.existsByNameIgnoreCase(name.trim())) {
            throw new BusinessException("Category name already exists");
        }
    }

    private String normalizeName(String name) {
        return name.trim();
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.isActive(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}