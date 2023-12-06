package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengeTag {

    ENVIRONMENT("환경 챌린지", "환경"),
    ROUTINE("습관 챌린지", "습관"),
    ETC("기타", "기타");

    public final String challengeTag;
    private final String frontStr;
}
