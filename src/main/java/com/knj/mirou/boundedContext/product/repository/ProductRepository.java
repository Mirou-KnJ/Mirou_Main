package com.knj.mirou.boundedContext.product.repository;

import com.knj.mirou.boundedContext.product.model.entity.Product;
import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
import com.knj.mirou.boundedContext.product.model.enums.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT DISTINCT p.info.id FROM Product p")
    List<Long> findDistinctProductIds();

    List<Product> findAllByInfoAndStatus(ProductInfo info, ProductStatus status);

    int countByInfoAndStatus(ProductInfo info, ProductStatus status);
}
