package com.knj.mirou.boundedContext.imageData.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OptimizerOption {

    CHALLENGE_DETAIL("?type=m&w=450&h=200&quality=90&bgcolor=FFFFFF&ttype=png"),
    CHALLENGE_LIST("?type=m&w=150&h=150&quality=90&bgcolor=FFFFFF&ttype=png"),
    FEED_MODAL("?type=m&w=111&h=80&quality=90&bgcolor=FFFFFF&ttype=png"),
    HISTORY("?type=m&w=50&h=50&quality=100&bgcolor=FFFFFF&ttype=png"),
    FEED_PROFILE("?type=m&w=24&h=24&quality=90&bgcolor=FFFFFF&ttype=png"),
    MY_PAGE_PROFILE("?type=m&w=96&h=96&quality=90&bgcolor=FFFFFF&ttype=png");

    private final String option;
}
