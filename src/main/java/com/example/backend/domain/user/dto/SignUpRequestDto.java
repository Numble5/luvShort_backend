package com.example.backend.domain.user.dto;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.embedded.UserInfo;
import com.example.backend.domain.user.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

    private String email;
    private String nickname;
    private String birthday;
    private String gender;
    private String city;
    private String district;
    private List<String> SelectedInterests;

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

    public User toEntity() {

        return User.builder()
                .email(email)
                .nickname(nickname)
                .userInfo( // 임베디드타입
                        UserInfo
                                .builder()
                                .age(Integer.parseInt(this.birthday))
                                .genderType(GenderType.valueOf(this.gender))
                                .city(this.city)
                                .district(this.district)
                                .build()
                )
                .build();
    }

}
