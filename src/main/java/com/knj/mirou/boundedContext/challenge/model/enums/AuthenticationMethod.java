package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum AuthenticationMethod {

    TEXT("텍스트 인증"),
    PHOTO("인증샷 인증"),
    LOCATION("위치 기반 인증"),
    ETC("기타");

    private final String method;

}
