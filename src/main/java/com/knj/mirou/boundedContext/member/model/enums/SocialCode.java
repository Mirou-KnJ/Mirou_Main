package com.knj.mirou.boundedContext.member.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialCode {

    KAKAO("카카오"),
    GOOGLE("구글"),
    NAVER("네이버"),
    ETC("기타");

    private final String socialCode;
}
