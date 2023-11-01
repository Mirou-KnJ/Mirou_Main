package com.knj.mirou.boundedContext.notification.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotiType {

    USE_POINT("포인트 사용"),
    GET_POINT("포인트 획득"),
    GET_PROD("상품 구매"),
    GET_REPORT("신고");

    private final String notiType;
}
