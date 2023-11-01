package com.knj.mirou.boundedContext.challenge.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChallengeStatus {

    OPEN(true),
    CLOSE(false);

    private final boolean status;
}
