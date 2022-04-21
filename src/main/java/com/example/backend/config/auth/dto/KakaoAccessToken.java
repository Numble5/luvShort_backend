package com.example.backend.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KakaoAccessToken {

    // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-request
    private String access_token;                       // 사용자 액세스 토큰 값
    private Integer expires_in;                       // 액세스 토큰과 ID 토큰의 만료 시간(초)
    private String refresh_token;                     // 사용자 리프레시 토큰 값
    private Integer refresh_token_expires_in;         // 리프레시 토큰 만료 시간(초)
    private String scope;                             // 인증된 사용자의 정보 조회 권한 범위
    private String token_type;                        // 토큰 타입, bearer로 고정

    @Builder
    public KakaoAccessToken(String access_token, Integer expires_in, String refresh_token, Integer refresh_token_expires_in, String scope, String token_type){
        this.access_token = access_token;
        this.expires_in = expires_in;
        this.refresh_token = refresh_token;
        this.refresh_token_expires_in = refresh_token_expires_in;
        this.scope = scope;
        this.token_type = token_type;
    }
}
