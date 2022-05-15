package com.example.backend.domain.user.dto;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.UserInterest;
import com.example.backend.domain.user.enums.GenderType;
import lombok.Builder;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Data
public class ProfileResponseDto {

    private String profileImg;

    private String nickname;
    private String birthday;
    private GenderType gender;
    private String city;
    private String district;

    private List<String> interests;
    private String introduce;

    // YYYYMMDD를 int age로 계산
    private int convertBirthdayToAge(String birthday){
        String today = ""; // 오늘 날짜
        int manAge = 0; // 만 나이

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");

        today = formatter.format(new Date()); // 시스템 날짜를 가져와서 yyyyMMdd 형태로 변환

        // today yyyyMMdd
        int todayYear = Integer.parseInt(today.substring(0, 4));
        int todayMonth = Integer.parseInt(today.substring(4, 6));
        int todayDay = Integer.parseInt(today.substring(6, 8));

        int birthdayYear = Integer.parseInt(birthday.substring(0, 4));
        int birthdayMonth = Integer.parseInt(birthday.substring(4, 6));
        int birthdayDay = Integer.parseInt(birthday.substring(6, 8));

        manAge = todayYear - birthdayYear;

        // 한국나이
        return manAge + 1;

    }

    @Builder
    public ProfileResponseDto(User profileUser){
        this.profileImg = profileUser.getProfile().getProfileImg();
        this.nickname = profileUser.getNickname();
        this.birthday = String.valueOf(profileUser.getUserInfo().getAge());
        this.gender = profileUser.getUserInfo().getGenderType();
        this.city = profileUser.getUserInfo().getCity();
        this.district = profileUser.getUserInfo().getDistrict();

        List<String> interestStr = new LinkedList<>();
        for(UserInterest userInterest: profileUser.getUserInterests()){
            interestStr.add(userInterest.getInterest().getInterestName());
        }
        this.interests = interestStr;
        this.introduce = profileUser.getProfile().getIntroduce();

    }


}
