package com.knj.mirou.base.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChangeType {

    USED("사용"),
    GET("적립");

    private final String changeType;
}
