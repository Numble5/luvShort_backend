package com.example.backend.domain.likes.dto;

import com.example.backend.domain.likes.Likes;
import lombok.Builder;
import lombok.Data;

@Data
public class PushHeartButtonResponseDto {

    private Long likes_idx;
    private Long user_idx;
    private Long video_idx;

    @Builder
    public PushHeartButtonResponseDto(Likes entity){
        this.likes_idx = entity.getIdx();
        this.user_idx = entity.getLikeUser().getIdx();
        this.video_idx = entity.getLikeVideo().getIdx();
    }
}
