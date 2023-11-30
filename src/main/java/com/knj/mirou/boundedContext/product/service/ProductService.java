package com.knj.mirou.boundedContext.product.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.product.model.entity.Product;
import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
import com.knj.mirou.boundedContext.product.repository.ProductRepository;
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
public class ProductService {

    private final ProductInfoService productInfoService;
    private final ProductRepository productRepository;

    public RsData<Long> create(long infoId, String code) {

        Optional<ProductInfo> OInfo = productInfoService.getById(infoId);
        if(OInfo.isEmpty()) {
            return RsData.of("F-1", "물품 정보가 유효하지 않습니다.");
        }

        Product product = new Product(OInfo.get(), code);
        Product savedProduct = productRepository.save(product);

        return RsData.of("S-1", "코드 등록에 성공하였습니다.", savedProduct.getId());
    }

    public List<ProductInfo> getAllProductInfo() {

        return productInfoService.getAll();
    }

}
