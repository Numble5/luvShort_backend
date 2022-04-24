package com.example.backend.dto;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.embedded.UserInfo;
import com.example.backend.domain.user.enums.GenderType;
import com.example.backend.domain.user.enums.SocialAccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    private String nickname;
    private int age;
    private GenderType gender;
    private String city;
    private String district;
    private List<String> interests;
    private String jwt;

    public User toEntity() {

        return User.builder()
                .nickname(nickname)
                .userInfo( // 임베디드타입
                        UserInfo
                                .builder()
                                .age(this.age)
                                .genderType(this.gender)
                                .city(this.city)
                                .district(this.district)
                                .build()
                )
                .build();
    }

}
