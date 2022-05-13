package com.example.backend.domain.user.embedded;

import com.example.backend.domain.user.Profile;
import com.example.backend.domain.user.dto.EditMyProfileDto;
import com.example.backend.domain.user.enums.GenderType;
import com.example.backend.domain.user.enums.RoleType;
import com.example.backend.domain.user.enums.SocialAccountType;
import com.example.backend.domain.user.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@NoArgsConstructor
public class UserInfo {

    private int age;
    private String city; // 시
    private String district; // 동

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @Enumerated(EnumType.STRING)
    private SocialAccountType socialAccountType;

    private Long socialId;

    @Builder
    public UserInfo(int age, String city, String district, GenderType genderType, SocialAccountType socialAccountType, Long socialId){
        this.age = age;
        this.city = city;
        this.district = district;
        this.genderType = genderType;
        this.socialAccountType = socialAccountType;
        this.socialId = socialId;
    }

    public void updateUserInfo(EditMyProfileDto editMyProfileDto){
        this.age = Integer.parseInt(editMyProfileDto.getBirthday());
        this.city = editMyProfileDto.getCity();
        this.district = editMyProfileDto.getDistrict();
        this.genderType = GenderType.valueOf(editMyProfileDto.getGender());
    }
}
