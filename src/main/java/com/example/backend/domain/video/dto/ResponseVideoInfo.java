package com.example.backend.domain.video.dto;

import com.example.backend.domain.user.UserInterest;
import com.example.backend.domain.video.Video;
import com.example.backend.domain.video.enums.VideoType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ResponseVideoInfo {
    // 비디오 정보
    private Long video_idx;
    private VideoType videoType;

    private String title;
    private String content;
    private Long hits;
    private String thumbnailUrl;
    private String videoUrl;

    private List<String> interest;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    // 사용자 정보
    private String nickname;
    private String profileImgUrl;

    @Builder
    public ResponseVideoInfo(Long idx, VideoType videoType, String title, String content, Long hits, String thumbnailUrl, String videoUrl, String nickname, String profileImgUrl,List<String> interest,LocalDateTime createdDate,LocalDateTime updatedDate) {
        this.video_idx = idx;
        this.videoType = videoType;
        this.title = title;
        this.content = content;
        this.hits = hits;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.nickname = nickname;
        this.profileImgUrl = profileImgUrl;
        this.interest = interest;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

}
