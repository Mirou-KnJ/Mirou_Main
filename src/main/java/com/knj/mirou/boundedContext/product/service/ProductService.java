package com.knj.mirou.boundedContext.product.service;

import com.knj.mirou.boundedContext.product.model.entity.Product;
import com.knj.mirou.boundedContext.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public void create(String name, String brandName, int cost, String content, String imgUrl, String usingWay, String usingCaution) {

        Product product = Product.builder()
                .name(name)
                .brandName(brandName)
                .cost(cost)
                .content(content)
                .imgUrl(imgUrl)
                .usingWay(usingWay)
                .usingCaution(usingCaution)
                .build();

        productRepository.save(product);
    }

}
