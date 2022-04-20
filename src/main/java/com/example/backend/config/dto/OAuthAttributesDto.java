package com.example.backend.config.dto;

import com.example.backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributesDto {
    // FIXME: 카카오 사용자 정보 어떤 형태로 주는지에 따라 코드 바꿔야 할수도
    private Map<String, Object> attributes;
    private String email;

    @Builder
    public OAuthAttributesDto(Map<String, Object> attributes, String email) {
        this.attributes = attributes;
        this.email = email;
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .build();
    }
}
