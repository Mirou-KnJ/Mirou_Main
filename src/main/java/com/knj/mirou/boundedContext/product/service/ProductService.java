package com.knj.mirou.boundedContext.product.service;

import com.knj.mirou.base.event.EventAfterBuyProduct;
import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.coin.service.CoinService;
import com.knj.mirou.boundedContext.inventory.service.InventoryService;
import com.knj.mirou.boundedContext.member.model.entity.Member;
import com.knj.mirou.boundedContext.product.model.entity.Product;
import com.knj.mirou.boundedContext.product.model.entity.ProductInfo;
import com.knj.mirou.boundedContext.product.model.enums.ProductStatus;
import com.knj.mirou.boundedContext.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final InventoryService inventoryService;
    private final CoinService coinService;
    private final ProductInfoService productInfoService;

    private final ApplicationEventPublisher publisher;

    private final ProductRepository productRepository;

    public RsData<Long> create(long infoId, String code) {

        Optional<ProductInfo> OInfo = productInfoService.getById(infoId);
        if (OInfo.isEmpty()) {
            return RsData.of("F-1", "물품 정보가 유효하지 않습니다.");
        }

        ProductInfo productInfo = OInfo.get();

        ProductStatus status = null;
        //해당 정보로 등록된 판매중인 상품이 이미 존재하다면 판매중으로 바로 투입, 아니면 판매 전으로 세팅
        if (getAllByInfoAndStatus(productInfo, ProductStatus.SALE).isEmpty()) {
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

    public List<ProductInfo> getAllRegisteredInfos() {

        List<Long> registeredIds = getRegisteredIds();

        return productInfoService.getAllRegisteredInfos(registeredIds);
    }

    public List<Product> getAllByInfoAndStatus(ProductInfo info, ProductStatus status) {

        return productRepository.findAllByInfoAndStatus(info, status);
    }

    public int getCountByInfoAndStatus(ProductInfo info, ProductStatus status) {

        return productRepository.countByInfoAndStatus(info, status);
    }

    public List<Integer> getRegisteredCounts(List<ProductInfo> productInfos) {

        List<Integer> counts = new ArrayList<>();

        for (ProductInfo info : productInfos) {
            counts.add(getCountByInfoAndStatus(info, ProductStatus.BEFORE_SALE));
        }

        return counts;
    }

    public List<Integer> getPossessionCounts(List<ProductInfo> productInfos) {

        List<Integer> counts = new ArrayList<>();

        for (ProductInfo info : productInfos) {
            counts.add(getCountByInfoAndStatus(info, ProductStatus.SALE));
        }

        return counts;
    }

    public Map<Long, Integer> getSalingCountMap(List<ProductInfo> productInfos) {

        Map<Long, Integer> counts = new HashMap<>();

        for (ProductInfo info : productInfos) {

            int count = productRepository.countByInfoAndStatus(info, ProductStatus.SALE);

            counts.put(info.getId(), count);
        }

        return counts;
    }

    @Transactional
    public RsData<String> startSale(long infoId) {

        Optional<ProductInfo> OInfo = productInfoService.getById(infoId);
        if (OInfo.isEmpty()) {
            return RsData.of("F-1", "상품 정보가 잘못되었습니다.");
        }

        ProductInfo productInfo = OInfo.get();

        List<Product> beforeProducts = getAllByInfoAndStatus(productInfo, ProductStatus.BEFORE_SALE);

        for (Product product : beforeProducts) {
            product.setStatus(ProductStatus.SALE);
        }

        return RsData.of("S-1", "판매가 시작되었습니다.");
    }

    @Transactional
    public RsData<String> tryBuy(long infoId, Member member) {

        Optional<ProductInfo> OInfo = productInfoService.getById(infoId);
        if (OInfo.isEmpty()) {
            return RsData.of("F-1", "상품 정보가 유효하지 않습니다.");
        }
        ProductInfo productInfo = OInfo.get();

        int currentCoin = member.getCoin().getCurrentCoin();
        if (productInfo.getCost() > currentCoin) {
            return RsData.of("F-2", "코인이 부족합니다.");
        }

        coinService.buyProduct(productInfo, member);

        Product targetProduct = getAllByInfoAndStatus(productInfo, ProductStatus.SALE).get(0);
        targetProduct.setStatus(ProductStatus.SOLD_OUT);

        inventoryService.create(targetProduct, member);

        publisher.publishEvent(new EventAfterBuyProduct(this, member, productInfo));

        return RsData.of("S-1", "구매에 성공하였습니다.");
    }
}
