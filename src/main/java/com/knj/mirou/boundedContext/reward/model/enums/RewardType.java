package com.knj.mirou.boundedContext.reward.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardType {

    COIN("코인"),
    PRODUCT("상품");

    private final String rewardType;
}
