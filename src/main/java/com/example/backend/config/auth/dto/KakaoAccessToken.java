package com.example.backend.config.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// FIXME: 필요없는 파일인데 다른 파일에서 쓰고 있는 객체라서 지우지 않았음. 코드 리팩토링 필요
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
