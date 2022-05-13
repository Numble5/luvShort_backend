package com.example.backend.domain.user.dto;

import com.example.backend.domain.user.Profile;
import com.example.backend.domain.user.User;
import com.example.backend.domain.user.embedded.UserInfo;
import com.example.backend.domain.user.enums.GenderType;
import com.example.backend.domain.user.enums.RoleType;
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

    public User toEntity() {

        return User.builder()
                .email(email)
                .nickname(nickname)
                .roleType(RoleType.NORMAL)
                .userInfo( // 임베디드타입
                        UserInfo
                                .builder()
                                .age(Integer.parseInt(this.birthday))
                                .genderType(GenderType.valueOf(this.gender))
                                .city(this.city)
                                .district(this.district)
                                .build()
                )
                // 주영) 이미지 자동 등록용
                .profile(
                       Profile
                               .builder()
                               .profileImg(GenderType.valueOf(this.gender) == GenderType.MALE?
                                       "https://numble-luvshort.s3.ap-northeast-2.amazonaws.com/profile-image/profile-m.jpeg" :
                                       "https://numble-luvshort.s3.ap-northeast-2.amazonaws.com/profile-image/profile-w.jpeg")
                               .build()
                )
                .build();
    }

}
