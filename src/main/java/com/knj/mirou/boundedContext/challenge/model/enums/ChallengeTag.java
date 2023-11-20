package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ChallengeTag {

    WATER("물 마시기"),
    BOOKS("책 읽기"),
    ENVIRONMENT("환경 챌린지"),
    ROUTINE("습관 챌린지"),
    ETC("기타");

    public final String challengeTag;

    public List<String> labels;

    public void setLabels(List<String> configLabels) {
        this.labels = configLabels;
    }
}
