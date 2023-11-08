package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengeStatus {

    BEFORE_SETTINGS("세팅 전"),
    OPEN("진행중"),
    CLOSE("종료");

    private final String status;
}
