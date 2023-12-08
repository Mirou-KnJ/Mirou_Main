package com.knj.mirou.boundedContext.challengefeed.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedStatus {

    PUBLIC("공개"),
    PRIVATE("비공개");

    private final String status;
}
