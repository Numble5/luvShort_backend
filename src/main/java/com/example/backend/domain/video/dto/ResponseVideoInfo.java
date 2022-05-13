package com.example.backend.domain.video.dto;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.UserInterest;
import com.example.backend.domain.user.dto.VideoUploaderDto;
import com.example.backend.domain.video.Video;
import com.example.backend.domain.video.VideoCategory;
import com.example.backend.domain.video.enums.ControlType;
import com.example.backend.domain.video.enums.VideoType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ResponseVideoInfo {
    // 비디오 정보
    private Long video_idx;
    private VideoType videoType;
    private ControlType controlType;

    private String title;
    private String content;
    private Long hits;
    private String thumbnailUrl;
    private String videoUrl;


    private List<String> categories;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private VideoUploaderDto uploader;

    private Boolean heart; //user가 이 비디오 좋아요 눌렀는지

    public void setHeart(Boolean heart){
        this.heart = heart;
    }

    @Builder
    public ResponseVideoInfo(Long idx, VideoType videoType,ControlType controlType, String title, String content, Long hits, String thumbnailUrl, String videoUrl, List<VideoCategory> categories, LocalDateTime createdDate, LocalDateTime updatedDate, User uploader, Boolean heart) {
        this.video_idx = idx;
        this.videoType = videoType;
        this.controlType = controlType;
        this.title = title;
        this.content = content;
        this.hits = hits;
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.heart = heart;

        // 카테고리 이름 return
        List<String> userCategories = new LinkedList<>();
        for(VideoCategory v: categories){
            userCategories.add(v.getCategory().getCategoryName());
        }
        this.categories = userCategories;

        // uploader 정보
        this.uploader = VideoUploaderDto.builder().user(uploader).build();

    }

}
