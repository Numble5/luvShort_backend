package com.example.backend.domain.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EditMyProfileDto {

    private String profileImg;
    private List<String> interests;
    private String nickname;
    private String birthday;
    private String gender;
    private String city;
    private String district;
    private String introduce;

}
