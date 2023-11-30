package com.knj.mirou.boundedContext.productinfo.repository;

import com.knj.mirou.boundedContext.productinfo.model.entity.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductInfoRepository extends JpaRepository<ProductInfo, Long> {
}
