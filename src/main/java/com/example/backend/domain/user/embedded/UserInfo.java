package com.example.backend.domain.user.embedded;

import com.example.backend.domain.user.enums.GenderType;
import com.example.backend.domain.user.enums.SocialAccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    private int age;
    private String location;

    @Enumerated(EnumType.STRING)
    private GenderType genderType;

    @Enumerated(EnumType.STRING)
    private SocialAccountType socialAccountType;

    private Long socialId;


}
