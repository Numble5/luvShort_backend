package com.example.backend.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SocialAccountType {

    KAKAO("카카오"),
    NAVER("네이버");

    private final String socialAccount;
}
