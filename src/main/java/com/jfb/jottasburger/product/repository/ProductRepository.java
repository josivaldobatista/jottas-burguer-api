package com.jfb.jottasburger.product.repository;

import com.jfb.jottasburger.product.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @EntityGraph(attributePaths = "category")
    List<Product> findByActiveTrueOrderByNameAsc();

    @EntityGraph(attributePaths = "category")
    List<Product> findAllByOrderByNameAsc();

    @Override
    @EntityGraph(attributePaths = "category")
    Optional<Product> findById(Long id);
}