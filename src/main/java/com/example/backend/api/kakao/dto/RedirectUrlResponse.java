package com.example.backend.api.kakao.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class RedirectUrlResponse {
    private String redirectUrl;

    @Builder
    public RedirectUrlResponse(String url){
        this.redirectUrl = url;
    }

}
