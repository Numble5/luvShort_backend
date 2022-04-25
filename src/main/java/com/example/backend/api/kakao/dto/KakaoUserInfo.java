package com.example.backend.api.kakao.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import net.minidev.json.JSONObject;

import java.time.LocalDateTime;


@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserInfo {
    // 카카오 REST API 공식문서 https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info

    private Long id;

    @JsonProperty("connected_at")
    private LocalDateTime connectedAt; //FIXME: UTC to KST

    @JsonProperty("kakao_account")
    private JSONObject kakaoAccount;

}
