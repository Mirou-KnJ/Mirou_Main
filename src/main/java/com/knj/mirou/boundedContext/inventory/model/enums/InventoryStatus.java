package com.knj.mirou.boundedContext.inventory.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InventoryStatus {

    BEFORE_USED("사용 전"),
    AFTER_USED("사용 후");

    private final String status;
}
