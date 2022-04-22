package com.example.backend.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// FIXME: 필요없는 파일인데 다른 파일에서 쓰고 있는 객체라서 지우지 않았음. 코드 리팩토링 필요
@NoArgsConstructor
@Getter
public class KakaoAccessTokenInfo {

    // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#get-token-info-response
    private Long id;                       // 회원번호
    private Integer expiresIn;             // 액세스 토큰 만료 시간(초)
    private Integer appId;                 // 토큰이 발급된 앱 ID

    @Builder
    public KakaoAccessTokenInfo(Long access_token, Integer expires_in, Integer app_id){
        this.id = access_token;
        this.expiresIn = expires_in;
        this.appId = app_id;
    }
}
