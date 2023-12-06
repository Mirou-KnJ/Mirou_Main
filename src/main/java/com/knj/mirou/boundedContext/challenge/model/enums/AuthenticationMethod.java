package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthenticationMethod {

    TEXT("텍스트 인증", "텍스트"),
    PHOTO("인증샷 인증", "인증샷"),
    LOCATION("위치 기반 인증", "위치"),
    ETC("기타", "기타");

    private final String method;
    private final String frontStr;
}
