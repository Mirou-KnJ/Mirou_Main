package com.knj.mirou.boundedContext.product.service;

import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
import com.knj.mirou.boundedContext.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductInfoService productInfoService;
    private final ProductRepository productRepository;

    public List<ProductInfo> getAllProductInfo() {

        return productInfoService.getAll();
    }

}
