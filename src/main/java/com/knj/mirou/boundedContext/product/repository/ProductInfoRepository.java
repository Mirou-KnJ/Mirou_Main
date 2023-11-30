package com.knj.mirou.boundedContext.product.repository;

import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {
}
