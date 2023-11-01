package com.knj.mirou.boundedContext.challengemember.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Progress {

    IN_PROGRESS(true),
    PROGRESS_END(false);

    private final boolean progress;
}
