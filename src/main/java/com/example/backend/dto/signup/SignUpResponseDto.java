package com.example.backend.dto.signup;

import lombok.Builder;

@Builder
public class SignUpResponseDto {

    private String nickname;
    private String jwt;
    private int status;

}
