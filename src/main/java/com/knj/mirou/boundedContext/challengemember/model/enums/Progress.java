package com.knj.mirou.boundedContext.challengemember.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Progress {

    IN_PROGRESS("진행중"),
    PROGRESS_END("진행 종료");

    private final String progress;
}
