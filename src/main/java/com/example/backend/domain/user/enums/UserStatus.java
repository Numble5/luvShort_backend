package com.example.backend.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    NORMAL("정상 회원"),
    LEAVE("탈퇴 회원");

    private final String status;

}
