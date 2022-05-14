package com.example.backend.api.kakao.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RedirectUrlResponse {

    private String redirectUrl;
    private String user_email;

    @Builder
    public RedirectUrlResponse(String url, String email){
        this.redirectUrl = url;
        this.user_email = email;
    }

}
