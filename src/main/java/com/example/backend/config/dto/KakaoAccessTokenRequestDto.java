package com.example.backend.config.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class KakaoAccessTokenRequestDto {

    // https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-request
    private String accessToken;

    @Builder
    public KakaoAccessTokenRequestDto(String accessToken){
        this.accessToken = accessToken;
    }
}
