package com.example.backend.domain.video.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class VideoUpdateDto {
    private Long idx;
    private String title;
    private String content;
    private List<String> categories;

    @Builder
    public VideoUpdateDto(Long idx, String title, String content, List<String> categories) {
        this.idx = idx;
        this.title = title;
        this.content = content;
        this.categories = categories;
    }
}
