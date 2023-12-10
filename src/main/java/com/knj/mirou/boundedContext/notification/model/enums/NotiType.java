package com.knj.mirou.boundedContext.notification.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotiType {

    JOIN("회원가입 완료"),
    JOIN_CHALLENGE("챌린지 참여"),
    END_PROGRESS("참여 종료"),
    GET_COIN("코인 획득"),
    RESET_POINT("이번 주 일상지원금 지급"),
    GET_PRODUCT("상품 구매"),
    USE_PRODUCT("상품 사용"),
    GET_REPORT("신고 당함"),
    REPORT_COUNT("지난 주 받은 신고 수"),
    LIKE_COUNT("지난 주 받은 좋아요 수");

    private final String notiType;
}
