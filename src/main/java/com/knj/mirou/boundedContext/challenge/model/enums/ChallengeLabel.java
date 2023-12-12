package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public enum ChallengeLabel {

    WATER("물 마시기 챌린지", false),
    BOOKS("책 읽기 챌린지", false),
    MUSIC("음악 한 곡 듣기 챌린지",false),
    VITAMIN("비타민 섭취 인증 챌린지",false),
    MORNING("아침 기상 인증 챌린지",false),
    BRUSH("양치 인증 챌린지",false),
    PLAN("오늘 TODO리스트 인증  챌린지",false),
    RECEIPT("오늘 지출 확인 챌린지",false),
    WEATHER("오늘 날씨 확인 챌린지",false),
    TEMPERATURE("실내 적정온도 인증 챌린지",false),
    PLANT("화분에 물 주기 챌린지",false),
    MOVIE("보고싶은 영화 인증 챌린지",false),
    ETC("기타 커스텀 챌린지", true);
    
    private final String name;
    private final boolean isCustom;
    private List<String> labels;

    public void setLabels(List<String> configLabels) {
        this.labels = configLabels;
    }
}
