package com.knj.mirou.boundedContext.store.service;

import com.knj.mirou.boundedContext.product.model.entity.Product;
import com.knj.mirou.boundedContext.product.service.ProductService;
import com.knj.mirou.boundedContext.store.repository.StroeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final ProductService productService;
    private final StroeRepository stroeRepository;

    public List<Product> getAllProducts() {

        return productService.getAll();
    }

}
