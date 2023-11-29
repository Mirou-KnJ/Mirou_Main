package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MapCategory {

    ATTRACTION("관광명소", "AT4"),
    CULTURE("문화시설", "CT1"),
    RESTAURANT("음식점", "FD6"),
    CAFE("카페", "CE7");

    private final String name;
    private final String code;
}