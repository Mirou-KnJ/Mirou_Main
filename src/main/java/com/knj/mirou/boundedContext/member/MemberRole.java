package com.knj.mirou.boundedContext.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {

    ADMIN("관리자"),
    USER("일반 회원");

    private final String role;
}
