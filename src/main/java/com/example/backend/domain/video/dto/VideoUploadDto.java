package com.example.backend.domain.video.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Getter
@Setter
public class VideoUploadDto {
    private String email;
    private String title;
    private String content;
    private String videoUrl;
    private String thumbUrl;
    private String videoType;
    private List<String> categories;

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
    public VideoUploadDto(String email, String title, String content, String videoUrl, String thumbUrl,String videoType,List<String> categories) {
        this.email = email;
        this.title = title;
        this.content = content;
        this.videoUrl = videoUrl;
        this.thumbUrl = thumbUrl;
        this.videoType = videoType;
        this.categories = categories;

    }
}
