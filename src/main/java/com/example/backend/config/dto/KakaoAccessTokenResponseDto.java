package com.example.backend.config.dto;

import com.example.backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class KakaoAccessTokenResponseDto {
    // FIXME: 카카오 사용자 정보 어떤 형태로 주는지에 따라 코드 바꿔야 할수도
    // 카카오 REST API 공식문서 https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
    private Map<String, Object> attributes;
    private String email;

    @Builder
    public KakaoAccessTokenResponseDto(Map<String, Object> attributes, String email) {
        this.attributes = attributes;
        this.email = email;
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .build();
    }
}
