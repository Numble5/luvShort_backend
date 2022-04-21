package com.example.backend.config.auth.dto;

import com.example.backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import net.minidev.json.JSONObject;

import java.time.LocalDateTime;

@Getter
public class KakaoUserInfoResponseDto {
    // 카카오 REST API 공식문서 https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
    private final Long id;
    private final Boolean has_signed_up;
    private final LocalDateTime connected_at; //FIXME: UTC to KST
    private final LocalDateTime synched_at;
    private final JSONObject properties;
    private final JSONObject KakaoAccount;

    private final String email = "example@example.com"; // FIXME

    @Builder
    public KakaoUserInfoResponseDto(Long id, Boolean has_signed_up, LocalDateTime connected_at, LocalDateTime synched_at, JSONObject properties, JSONObject KakaoAccount) {
        this.id = id;
        this.has_signed_up = has_signed_up;
        this.connected_at = connected_at;
        this.synched_at = synched_at;
        this.properties = properties;
        this.KakaoAccount = KakaoAccount;
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .build();
    }
}
