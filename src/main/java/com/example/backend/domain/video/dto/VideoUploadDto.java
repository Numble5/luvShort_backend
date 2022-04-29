package com.example.backend.domain.video.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
public class VideoUploadDto {
    String email;
    String title;
    String content;
    String videoUrl;
    String thumbUrl;
    String videoType;

    public VideoUploadDto toEntity() {
        VideoUploadDto build = VideoUploadDto.builder()
                .email(email)
                .title(title)
                .content(content)
                .videoUrl(videoUrl)
                .thumbUrl(thumbUrl)
                .videoType(videoType)
                .build();
        return build;
    }
    @Builder
    public VideoUploadDto(String email, String title, String content, String videoUrl, String thumbUrl,String videoType) {
        this.email = email;
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.thumbUrl = thumbUrl;
        this.videoType = videoType;
    }
}
