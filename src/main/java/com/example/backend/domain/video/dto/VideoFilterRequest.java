package com.example.backend.domain.video.dto;

import com.example.backend.domain.user.enums.GenderType;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoFilterRequest {
    private List<String> categories;

    private String gender;
    private String city;
    private String district;

}
