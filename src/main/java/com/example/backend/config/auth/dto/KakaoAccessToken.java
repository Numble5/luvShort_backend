package com.example.backend.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KakaoAccessToken {

    // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-request
    private String accessToken;                       // 사용자 액세스 토큰 값

    @Builder
    public KakaoAccessToken(String access_token){
        this.accessToken = access_token;
    }
}
