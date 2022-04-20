package com.example.backend.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public enum RoleType {

    // 스프링 시큐리티에서는 권한 코드에 항상 ROLE_이 있어야함
    // 나중에 스프링 시큐리티 쓸 걸 대비해서 붙임
    NORMAL("ROLE_NORMAL", "일반회원"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;
}
