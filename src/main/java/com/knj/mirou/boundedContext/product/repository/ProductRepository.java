package com.knj.mirou.boundedContext.product.repository;

import com.knj.mirou.boundedContext.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
