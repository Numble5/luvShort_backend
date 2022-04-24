package com.example.backend.domain.user.dto;

import lombok.Builder;

@Builder
public class SignUpResponseDto {

    private String nickname;
    private int status;

}
