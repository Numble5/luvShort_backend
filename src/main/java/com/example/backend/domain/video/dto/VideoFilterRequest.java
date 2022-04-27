package com.example.backend.domain.video.dto;

import com.example.backend.domain.user.enums.GenderType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoFilterRequest {
    private String category1;
    private String category2;
    private String category3;

    private String gender;
    private String city;
    private String district;

}
