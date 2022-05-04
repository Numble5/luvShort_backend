package com.example.backend.domain.user.dto;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.enums.GenderType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@Getter
public class UserReponseDtoByCookie {

    private Long idx;
    private String email;
    private String nickname;

    // userinfo
    private String birthday; // 벡엔드에서는 age, 프론트에서는 birthday로 씀
    private String city; // 시
    private String district; // 동
    private GenderType genderType;
    private List<String> interests;

    @Builder
    public UserReponseDtoByCookie(User entity, List<String> interestStr){
        this.idx = entity.getIdx();
        this.email = entity.getEmail();
        this.nickname = entity.getNickname();
        this.birthday = Integer.toString(entity.getUserInfo().getAge());
        this.city = entity.getUserInfo().getCity();
        this.district = entity.getUserInfo().getDistrict();
        this.genderType = entity.getUserInfo().getGenderType();
        this.interests = interestStr;
    }

}
