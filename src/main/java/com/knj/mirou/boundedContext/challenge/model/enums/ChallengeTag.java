package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengeTag {

    ENVIRONMENT("환경 챌린지", "환경"),
    ROUTINE("습관 챌린지", "습관"),
    CULTURE("문화 챌린지", "문화"),
    HEALTH("건강 챌린지", "건강"),
    ETC("기타", "기타");

    public final String challengeTag;
    private final String frontStr;
}
