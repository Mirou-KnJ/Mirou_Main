package com.knj.mirou.boundedContext.challenge.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengePeriod {

    DAY1("하루 챌린지"),
    DAY3("3일 챌린지"),
    DAY5("5일 챌린지"),
    DAY7("일주일 챌린지");

    private final String period;
}
