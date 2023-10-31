package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengeStatus {

    OPEN("진행중인 챌린지"),
    CLOSE("종료된 챌린지");

    private final String status;
}
