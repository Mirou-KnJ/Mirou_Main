package com.knj.mirou.boundedContext.product.repository;

import com.knj.mirou.boundedContext.product.model.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p.info.id FROM Product p")
    List<Long> findDistinctProductIds();
}
