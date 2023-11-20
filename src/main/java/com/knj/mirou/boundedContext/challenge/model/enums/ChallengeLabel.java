package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ChallengeLabel {

    WATER("물 마시기 챌린지", false),
    BOOKS("책 읽기 챌린지", false),
    ETC("기타 커스텀 챌린지", true);
    
    private final String name;
    private final boolean isCustom;
    private List<String> labels;

    public void setLabels(List<String> configLabels) {
        this.labels = configLabels;
    }
}
