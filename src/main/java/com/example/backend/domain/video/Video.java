package com.example.backend.domain.video;

import com.example.backend.domain.BaseEntity;
import com.example.backend.domain.user.User;
import com.example.backend.domain.video.enums.VideoType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;


@Getter
@NoArgsConstructor
@Entity
@Table
public class Video extends BaseEntity {

    @Id
    @Column(name = "video_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Enumerated(EnumType.STRING)
    private VideoType videoType;

    private String title;
    private String content;
    private Long hits;
    private String thumbnailUrl;
    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = false)
    private User uploader;

    @OneToMany(mappedBy = "video")
    private List<VideoCategory> Categories = new LinkedList<>(); // 해당 영상이 속한 카테고리들(여러개가능)

    @Builder
    public Video(String title, String content, Long hits, String thumbnailUrl, String videoUrl, User uploader){
        this.title = title;
        this.content = content;
        this.hits = Long.valueOf("0"); // 조회수는 0으로 초기화
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.uploader = uploader;
    }

    // 연관관계 매핑
    public void setUser(User user){
        // 기존 업로더와의 관계를 제거
        if (this.uploader != null){
            this.uploader.getMyVideos().remove(this);
        }
        this.uploader = user;
        uploader.getMyVideos().add(this);
    }

}
