package com.knj.mirou.boundedContext.store.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SaleType {

    SALE_PROD("판매 상품"),
    PRIZE_PROD("당첨 상품");

    private final String saleType;
}
