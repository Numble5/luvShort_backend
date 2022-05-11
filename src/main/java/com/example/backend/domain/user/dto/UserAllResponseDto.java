package com.example.backend.domain.user.dto;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.UserInterest;
import com.example.backend.domain.user.enums.GenderType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@Getter
public class UserAllResponseDto {

    private Long idx;
    private String email;
    private String nickname;

    // userinfo
    private int age;
    private String city; // 시
    private String district; // 동
    private GenderType genderType;

    // 프로필
    private String profileImg;

    // interest
    private List<String> interests;
    // 생성,수정 시간
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    @Builder
    public UserAllResponseDto(User entity){
        this.idx = entity.getIdx();
        this.email = entity.getEmail();
        this.nickname = entity.getNickname();
        this.age = entity.getUserInfo().getAge();
        this.city = entity.getUserInfo().getCity();
        this.district = entity.getUserInfo().getDistrict();
        this.genderType = entity.getUserInfo().getGenderType();
        this.profileImg = entity.getProfile().getProfileImg();
        this.createdTime = entity.getCreatedDate();
        this.updatedTime = entity.getUpdatedDate();

        List<String> inter = new LinkedList<>();
        for(UserInterest i: entity.getUserInterests()) {
            inter.add(i.getInterest().getInterestName());
        }
        this.interests = inter;
    }


}
