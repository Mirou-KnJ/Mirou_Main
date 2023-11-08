package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengePeriod {

    DAY1(1),
    DAY3(3),
    DAY5(5),
    DAY7(7);

    private final int period;
}
