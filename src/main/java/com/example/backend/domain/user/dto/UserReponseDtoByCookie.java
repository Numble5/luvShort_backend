package com.example.backend.domain.user.dto;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.enums.GenderType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Getter
public class UserReponseDtoByCookie {

    private Long idx;
    private String email;
    private String nickname;

    // userinfo
    private int age;
    private String city; // 시
    private String district; // 동
    private GenderType genderType;

    @Builder
    public UserReponseDtoByCookie(User entity){
        this.idx = entity.getIdx();
        this.email = entity.getEmail();
        this.nickname = entity.getNickname();
        this.age = entity.getUserInfo().getAge();
        this.city = entity.getUserInfo().getCity();
        this.district = entity.getUserInfo().getDistrict();
        this.genderType = entity.getUserInfo().getGenderType();
    }

}
