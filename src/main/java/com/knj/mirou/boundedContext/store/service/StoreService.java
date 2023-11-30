package com.knj.mirou.boundedContext.store.service;

import com.knj.mirou.base.rsData.RsData;
import com.knj.mirou.boundedContext.productinfo.model.entity.ProductInfo;
import com.knj.mirou.boundedContext.productinfo.service.ProductInfoService;
import com.knj.mirou.boundedContext.store.model.entity.Store;
import com.knj.mirou.boundedContext.store.model.enums.SaleType;
import com.knj.mirou.boundedContext.store.repository.StoreRepository;
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
public class StoreService {

    private final ProductInfoService productInfoService;
    private final StoreRepository storeRepository;

    public List<ProductInfo> getAllProducts() {

        return productInfoService.getAll();
    }

    public RsData<String> create(long productId, String saleType) {

        Optional<ProductInfo> OProduct = productInfoService.getById(productId);
        if(OProduct.isEmpty()) {
            return RsData.of("F-1", "대상 상품을 찾을 수 없습니다.");
        }

        Store store = Store.builder()
                .productInfo(OProduct.get())
                .saleType(SaleType.valueOf(saleType))
                .build();

        storeRepository.save(store);

        return RsData.of("S-1", "새 상품 등록에 성공하였습니다.");
    }

    public List<Store> getAllStoreProduct() {

        //FIXME
        return storeRepository.findAllByProductName("스타벅스");
    }

}
