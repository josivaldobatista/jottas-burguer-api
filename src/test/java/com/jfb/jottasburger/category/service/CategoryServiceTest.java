package com.jfb.jottasburger.category.service;

import com.jfb.jottasburger.category.dto.CategoryResponse;
import com.jfb.jottasburger.category.dto.CreateCategoryRequest;
import com.jfb.jottasburger.category.dto.UpdateCategoryRequest;
import com.jfb.jottasburger.category.model.Category;
import com.jfb.jottasburger.category.repository.CategoryRepository;
import com.jfb.jottasburger.exception.BusinessException;
import com.jfb.jottasburger.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldCreateCategorySuccessfully() {
        CreateCategoryRequest request = new CreateCategoryRequest(" Burgers ");

        when(categoryRepository.existsByNameIgnoreCase("Burgers")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            setField(category, "id", 1L);
            setField(category, "createdAt", Instant.now());
            setField(category, "updatedAt", Instant.now());
            return category;
        });

        CategoryResponse response = categoryService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Burgers", response.name());
        assertTrue(response.active());

        verify(categoryRepository).existsByNameIgnoreCase("Burgers");
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void shouldThrowWhenCategoryNameAlreadyExists() {
        CreateCategoryRequest request = new CreateCategoryRequest("Burgers");

        when(categoryRepository.existsByNameIgnoreCase("Burgers")).thenReturn(true);

        assertThrows(BusinessException.class, () -> categoryService.create(request));

        verify(categoryRepository).existsByNameIgnoreCase("Burgers");
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void shouldFindCategoryByIdSuccessfully() {
        Category category = new Category("Burgers");
        setField(category, "id", 1L);
        setField(category, "createdAt", Instant.now());
        setField(category, "updatedAt", Instant.now());

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        CategoryResponse response = categoryService.findById(1L);

        assertEquals(1L, response.id());
        assertEquals("Burgers", response.name());
        assertTrue(response.active());
    }

    @Test
    void shouldThrowWhenCategoryNotFoundById() {
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.findById(99L));
    }

    @Test
    void shouldReturnActiveCategoriesOrderedByName() {
        Category drinks = new Category("Drinks");
        setField(drinks, "id", 1L);
        setField(drinks, "createdAt", Instant.now());
        setField(drinks, "updatedAt", Instant.now());

        Category burgers = new Category("Burgers");
        setField(burgers, "id", 2L);
        setField(burgers, "createdAt", Instant.now());
        setField(burgers, "updatedAt", Instant.now());

        when(categoryRepository.findByActiveTrueOrderByNameAsc())
                .thenReturn(List.of(burgers, drinks));

        List<CategoryResponse> response = categoryService.findAllActive();

        assertEquals(2, response.size());
        assertEquals("Burgers", response.get(0).name());
        assertEquals("Drinks", response.get(1).name());
    }

    @Test
    void shouldUpdateCategorySuccessfully() {
        Category category = new Category("Burgers");
        setField(category, "id", 1L);
        setField(category, "createdAt", Instant.now());
        setField(category, "updatedAt", Instant.now());

        UpdateCategoryRequest request = new UpdateCategoryRequest(" Sandwiches ");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameIgnoreCase("Sandwiches")).thenReturn(false);

        CategoryResponse response = categoryService.update(1L, request);

        assertEquals(1L, response.id());
        assertEquals("Sandwiches", response.name());
    }

    @Test
    void shouldThrowWhenUpdatingCategoryToExistingName() {
        Category category = new Category("Burgers");
        setField(category, "id", 1L);
        setField(category, "createdAt", Instant.now());
        setField(category, "updatedAt", Instant.now());

        UpdateCategoryRequest request = new UpdateCategoryRequest("Drinks");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByNameIgnoreCase("Drinks")).thenReturn(true);

        assertThrows(BusinessException.class, () -> categoryService.update(1L, request));
    }

    @Test
    void shouldActivateCategorySuccessfully() {
        Category category = new Category("Burgers");
        category.deactivate();

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.activate(1L);

        assertTrue(category.isActive());
    }

    @Test
    void shouldDeactivateCategorySuccessfully() {
        Category category = new Category("Burgers");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        categoryService.deactivate(1L);

        assertFalse(category.isActive());
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = findField(target.getClass(), fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Field findField(Class<?> type, String fieldName) throws NoSuchFieldException {
        Class<?> current = type;
        while (current != null) {
            try {
                return current.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ignored) {
                current = current.getSuperclass();
            }
        }
        throw new NoSuchFieldException(fieldName);
    }
}