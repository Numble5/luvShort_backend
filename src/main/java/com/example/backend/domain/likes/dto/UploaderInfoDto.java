package com.example.backend.domain.likes.dto;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.enums.GenderType;
import lombok.Builder;
import lombok.Data;

@Data
public class UploaderInfoDto {

    private String email;
    private String nickname;

    // userInfo
    private int age;
    private String city; // 시
    private String district; // 동
    private GenderType genderType;

    // Profile
    private String profileImg;


    @Builder
    public UploaderInfoDto(User entity){

        this.email = entity.getEmail();
        this.nickname = entity.getNickname();
        this.age = entity.getUserInfo().getAge();
        this.city = entity.getUserInfo().getCity();
        this.district = entity.getUserInfo().getDistrict();
        this.genderType = entity.getUserInfo().getGenderType();
        this.profileImg = entity.getProfile().getProfileImg();

    }

}
