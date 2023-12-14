package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ChallengeLabel {

    WATER("물 마시기", false),
    BOOKS("책 읽기", false),
    MUSIC("음악 듣기", false),
    VITAMIN("비타민", false),
    MORNING("기상 인증", false),
    BRUSH("양치 인증", false),
    PLAN("할 일 인증", false),
    RECEIPT("지출 인증", false),
    WEATHER("날씨 확인", false),
    TEMPERATURE("적정온도 인증", false),
    PLANT("화분에 물 주기", false),
    MOVIE("영화 인증", false),
    ETC("기타 커스텀", true);

    private final String name;
    private final boolean isCustom;
    private List<String> labels;

    public void setLabels(List<String> configLabels) {
        this.labels = configLabels;
    }
}
