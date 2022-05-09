package com.example.backend.domain.likes.dto;

import com.example.backend.domain.likes.Likes;
import com.example.backend.domain.user.User;
import lombok.Builder;
import lombok.Data;

@Data
public class PushHeartButtonResponseDto {

    private Long likes_idx;
    private String user_email;
    private Long video_idx;
    private UploaderInfoDto uploader;

    @Builder
    public PushHeartButtonResponseDto(Likes entity){
        this.likes_idx = entity.getIdx();
        this.user_email = entity.getLikeUser().getEmail();
        this.video_idx = entity.getLikeVideo().getIdx();
        this.uploader = new UploaderInfoDto(entity.getLikeVideo().getUploader());
    }
}
