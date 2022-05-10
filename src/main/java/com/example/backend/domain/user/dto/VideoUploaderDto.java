package com.example.backend.domain.user.dto;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.UserInterest;
import com.example.backend.domain.user.enums.GenderType;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
public class VideoUploaderDto {

    // 사용자 정보
    private Long idx;
    private String email;
    private String nickname;
    private String profileImgUrl;
    private List<String> interest;
    private GenderType gender;
    private String city;
    private String district;

    @Builder
    public VideoUploaderDto(User user) {
        this.idx = user.getIdx();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImgUrl = user.getProfile() != null ? user.getProfile().getProfileImg(): "";
        this.gender = user.getUserInfo().getGenderType();
        this.city = user.getUserInfo().getCity();
        this.district = user.getUserInfo().getDistrict();

        List<String> interests = new ArrayList<>();
        for(UserInterest ui: user.getUserInterests()) {
            interests.add(ui.getInterest().getInterestName());
        }
        this.interest = interests;
    }
}
