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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldCreateProductSuccessfully() {
        Category category = new Category("Burgers");
        setField(category, "id", 10L);
        setField(category, "createdAt", Instant.now());
        setField(category, "updatedAt", Instant.now());

        CreateProductRequest request = new CreateProductRequest(
                " X-Burger ",
                " Burger with cheese ",
                new BigDecimal("25.00"),
                10L
        );

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product product = invocation.getArgument(0);
            setField(product, "id", 1L);
            setField(product, "createdAt", Instant.now());
            setField(product, "updatedAt", Instant.now());
            return product;
        });

        ProductResponse response = productService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("X-Burger", response.name());
        assertEquals("Burger with cheese", response.description());
        assertEquals(new BigDecimal("25.00"), response.price());
        assertTrue(response.active());
        assertEquals(10L, response.categoryId());
        assertEquals("Burgers", response.categoryName());
    }

    @Test
    void shouldThrowWhenCategoryDoesNotExistOnCreate() {
        CreateProductRequest request = new CreateProductRequest(
                "X-Burger",
                "Burger with cheese",
                new BigDecimal("25.00"),
                99L
        );

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.create(request));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldThrowWhenCategoryIsInactiveOnCreate() {
        Category category = new Category("Burgers");
        category.deactivate();

        CreateProductRequest request = new CreateProductRequest(
                "X-Burger",
                "Burger with cheese",
                new BigDecimal("25.00"),
                10L
        );

        when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));

        assertThrows(BusinessException.class, () -> productService.create(request));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void shouldFindProductByIdSuccessfully() {
        Category category = new Category("Burgers");
        setField(category, "id", 10L);
        setField(category, "createdAt", Instant.now());
        setField(category, "updatedAt", Instant.now());

        Product product = new Product(
                "X-Burger",
                "Burger with cheese",
                new BigDecimal("25.00"),
                category
        );
        setField(product, "id", 1L);
        setField(product, "createdAt", Instant.now());
        setField(product, "updatedAt", Instant.now());

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.findById(1L);

        assertEquals(1L, response.id());
        assertEquals("X-Burger", response.name());
        assertEquals("Burgers", response.categoryName());
    }

    @Test
    void shouldThrowWhenProductNotFoundById() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.findById(99L));
    }

    @Test
    void shouldReturnAllActiveProducts() {
        Category category = new Category("Burgers");
        setField(category, "id", 10L);
        setField(category, "createdAt", Instant.now());
        setField(category, "updatedAt", Instant.now());

        Product product = new Product(
                "X-Burger",
                "Burger with cheese",
                new BigDecimal("25.00"),
                category
        );
        setField(product, "id", 1L);
        setField(product, "createdAt", Instant.now());
        setField(product, "updatedAt", Instant.now());

        when(productRepository.findByActiveTrueOrderByNameAsc())
                .thenReturn(List.of(product));

        List<ProductResponse> response = productService.findAllActive();

        assertEquals(1, response.size());
        assertEquals("X-Burger", response.get(0).name());
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        Category oldCategory = new Category("Burgers");
        setField(oldCategory, "id", 10L);
        setField(oldCategory, "createdAt", Instant.now());
        setField(oldCategory, "updatedAt", Instant.now());

        Category newCategory = new Category("Premium Burgers");
        setField(newCategory, "id", 20L);
        setField(newCategory, "createdAt", Instant.now());
        setField(newCategory, "updatedAt", Instant.now());

        Product product = new Product(
                "X-Burger",
                "Burger with cheese",
                new BigDecimal("25.00"),
                oldCategory
        );
        setField(product, "id", 1L);
        setField(product, "createdAt", Instant.now());
        setField(product, "updatedAt", Instant.now());

        UpdateProductRequest request = new UpdateProductRequest(
                " X-Salad ",
                " Burger with salad ",
                new BigDecimal("28.00"),
                20L
        );

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(20L)).thenReturn(Optional.of(newCategory));

        ProductResponse response = productService.update(1L, request);

        assertEquals("X-Salad", response.name());
        assertEquals("Burger with salad", response.description());
        assertEquals(new BigDecimal("28.00"), response.price());
        assertEquals(20L, response.categoryId());
        assertEquals("Premium Burgers", response.categoryName());
    }

    @Test
    void shouldActivateProductSuccessfully() {
        Category category = new Category("Burgers");

        Product product = new Product(
                "X-Burger",
                "Burger with cheese",
                new BigDecimal("25.00"),
                category
        );
        product.deactivate();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.activate(1L);

        assertTrue(product.isActive());
    }

    @Test
    void shouldDeactivateProductSuccessfully() {
        Category category = new Category("Burgers");

        Product product = new Product(
                "X-Burger",
                "Burger with cheese",
                new BigDecimal("25.00"),
                category
        );

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.deactivate(1L);

        assertFalse(product.isActive());
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