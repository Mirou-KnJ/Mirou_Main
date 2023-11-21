package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengeTag {

    ENVIRONMENT("환경 챌린지"),
    ROUTINE("습관 챌린지"),
    ETC("기타");

    public final String challengeTag;
}
