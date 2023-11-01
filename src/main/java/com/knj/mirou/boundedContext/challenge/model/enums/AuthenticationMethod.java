package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthenticationMethod {

    TEXT("텍스트 인증"),
    PHOTO("인증샷 인증"),
    ETC("기타");

    private final String method;
}
