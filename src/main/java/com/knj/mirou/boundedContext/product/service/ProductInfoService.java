package com.knj.mirou.boundedContext.product.service;

import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
import com.knj.mirou.boundedContext.product.repository.ProductInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductInfoService {

    private final ProductInfoRepository productInfoRepository;

    @Transactional
    public void create(String name, String brandName, int cost, String content, String imgUrl, String usingWay, String usingCaution) {

        ProductInfo productInfo = ProductInfo.builder()
                .name(name)
                .brandName(brandName)
                .cost(cost)
                .content(content)
                .imgUrl(imgUrl)
                .usingWay(usingWay)
                .usingCaution(usingCaution)
                .build();

        productInfoRepository.save(productInfo);
    }

    public List<ProductInfo> getAll() {

        return productInfoRepository.findAll();
    }

    public Optional<ProductInfo> getById(long productId) {

        return productInfoRepository.findById(productId);
    }
}
