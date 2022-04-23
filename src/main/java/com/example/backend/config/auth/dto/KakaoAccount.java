package com.example.backend.config.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import net.minidev.json.JSONObject;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoAccount {

    private Boolean emailNeedsAgreement;
    private Boolean isEmailValid;
    private Boolean isEmailVerified;
    private String email;

    @Builder
    public KakaoAccount(JSONObject kakaoAccount){
        this.emailNeedsAgreement = (Boolean) kakaoAccount.get("email_needs_agreement");
        this.isEmailValid = (Boolean) kakaoAccount.get("is_email_valid");
        this.isEmailVerified = (Boolean) kakaoAccount.get("is_email_verified");
        this.email = (String) kakaoAccount.get("email");
    }

    /*
    @JsonProperty("kakao_account.email_needs_agreement")
    private Boolean emailNeedsAgreement;

    @JsonProperty("kakao_account.is_email_valid")
    private Boolean isEmailValid;

    @JsonProperty("kakao_account.is_email_verified")
    private Boolean isEmailVerified;

    @JsonProperty("kakao_account.email")
    private String email;

     */
}
