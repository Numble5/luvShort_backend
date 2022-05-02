package com.example.backend.domain.user.dto;

import com.example.backend.domain.user.User;
import lombok.Builder;
import lombok.Data;

@Data
public class SignUpResponseDto {

    private Long idx;
    private String nickname;
    private String email;

    @Builder
    public SignUpResponseDto(User entity){
        this.idx = entity.getIdx();
        this.nickname = entity.getNickname();
        this.email = entity.getEmail();
    }

}
