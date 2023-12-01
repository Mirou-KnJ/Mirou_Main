package com.knj.mirou.boundedContext.product.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProductStatus {

    BEFORE_SALE("판매전"),
    SALE("판매중"),
    SOLD_OUT("판매완료");

    private final String status;
}
