package com.knj.mirou.boundedContext.imageData.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OptimizerOption {

    CHALLENGE_DETAIL("?type=m&w=450&h=200&quality=90&bgcolor=FFFFFF&ttype=png"),
    FEED_DETAIL("feedDetail");

    private final String option;
}
