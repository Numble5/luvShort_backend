package com.example.backend.config.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import net.minidev.json.JSONObject;

import java.time.LocalDateTime;
import java.util.Map;


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

    /*
    private Boolean emailNeedsAgreement;
    private Boolean isEmailValid;
    private Boolean isEmailVerified;
    private String email;


    private void unpackKakaoAccountFromNestedObject(JSONObject kakaoAccount) {
        this.emailNeedsAgreement = (Boolean) kakaoAccount.get("email_needs_agreement");
        this.isEmailValid = (Boolean) kakaoAccount.get("is_email_valid");
        this.isEmailVerified = (Boolean) kakaoAccount.get("is_email_verified");
        this.email = (String) kakaoAccount.get("email");
    }

     */
    /*
    @JsonProperty("kakao_account")
    private void unpackKakaoAccountFromNestedObject(Map<String, Object> emailInfo){
        emailNeedsAgreement = (Boolean) emailInfo.get("email_needs_agreement");
        isEmailValid = (Boolean) emailInfo.get("is_email_valid");
        isEmailVerified = (Boolean) emailInfo.get("is_email_verified");
        email = (String) emailInfo.get("email");
    }

     */


    /* null
    @JsonProperty("kakao_account.email_needs_agreement")
    private Boolean emailNeedsAgreement;

    @JsonProperty("kakao_account.is_email_valid")
    private Boolean isEmailValid;

    @JsonProperty("kakao_account.is_email_verified")
    private Boolean isEmailVerified;

    @JsonProperty("kakao_account.email")
    private String email;
     */

    /*
    @JsonProperty("kakao_account")
    private JSONObject kakaoAccount;

    private Boolean emailNeedsAgreement;
    private Boolean isEmailValid;
    private Boolean isEmailVerified;
    private String email;

    @Builder
    public KakaoUserInfo(Long id, LocalDateTime connected_at, JSONObject kakao_account){
        this.id = id;
        this.connectedAt = connected_at;
        this.emailNeedsAgreement = (Boolean) kakao_account.get("email_needs_agreement");
        this.isEmailValid = (Boolean) kakao_account.get("email_needs_agreement");
        (Boolean) kakao_account.get("email_needs_agreement");
        (Boolean) kakao_account.get("email_needs_agreement");
    }
    */


     /*
    @JsonProperty("kakao_account")
    private List<String> kakaoAccounts;
    */

}
