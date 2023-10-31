package com.knj.mirou.boundedContext.reward.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RewardType {

    Coin("코인"),
    Product("상품");

    private final String rewardType;
}
