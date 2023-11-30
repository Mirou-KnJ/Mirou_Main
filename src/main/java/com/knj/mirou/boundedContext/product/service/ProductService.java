package com.knj.mirou.boundedContext.product.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.product.model.entity.Product;
import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
import com.knj.mirou.boundedContext.product.model.enums.ProductStatus;
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

        ProductInfo productInfo = OInfo.get();

        ProductStatus status = null;
        //해당 정보로 등록된 판매중인 상품이 이미 존재하다면 판매중으로 바로 투입, 아니면 판매 전으로 세팅
        if(getAllByInfoAndStatus(productInfo, ProductStatus.SALE).isEmpty()) {
            status = ProductStatus.BEFORE_SALE;
        } else {
            status = ProductStatus.SALE;
        }

        Product product = new Product(productInfo, code, status);
        Product savedProduct = productRepository.save(product);

        return RsData.of("S-1", "코드 등록에 성공하였습니다.", savedProduct.getId());
    }

    public List<ProductInfo> getAllProductInfo() {

        return productInfoService.getAll();
    }

    public List<Long> getRegisteredIds() {

        return productRepository.findDistinctProductIds();
    }

    public List<Product> getAllByInfoAndStatus(ProductInfo info, ProductStatus status) {

        return productRepository.findAllByInfoAndStatus(info, status);
    }

}
