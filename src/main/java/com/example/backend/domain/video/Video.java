package com.example.backend.domain.video;

import com.example.backend.domain.BaseEntity;
import com.example.backend.domain.likes.Likes;
import com.example.backend.domain.user.User;
import com.example.backend.domain.video.dto.VideoUpdateDto;
import com.example.backend.domain.video.enums.ControlType;
import com.example.backend.domain.video.enums.VideoType;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;


import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class Video extends BaseEntity {

    @Id
    @Column(name = "video_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Enumerated(EnumType.STRING)
    private VideoType videoType;

    @Enumerated(EnumType.STRING)
    private ControlType controlType;

    private String fileName;
    private String title;
    private String content;
    private Long hits;
    private String thumbnailUrl;
    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx", nullable = false)
    private User uploader;

    @OneToMany(mappedBy = "video",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true) // Video 삭제시 category 관계들 고아되어 삭제
    private List<VideoCategory> Categories = new LinkedList<>(); // 해당 영상이 속한 카테고리들(여러개가능)

    @OneToMany(mappedBy = "likeVideo",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private List<Likes> likesList = new LinkedList<>();

    @Builder
    public Video(String fileName,String title, String content, Long hits, String thumbnailUrl, String videoUrl, User uploader,VideoType videoType){
        this.fileName = fileName;
        this.title = title;
        this.content = content;
        this.hits = Long.valueOf("0"); // 조회수는 0으로 초기화
        this.thumbnailUrl = thumbnailUrl;
        this.videoUrl = videoUrl;
        this.videoType = videoType;
        this.controlType = ControlType.AVAIL; // 처음 생성 시 무조건 이용 가능 상태
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

    // 카테고리 추가
    // 엔티티가 컨텍스트 당 분리되지 않으면 (즉, 찾기 및 업데이트 작업이 동일한 트랜잭션에 있을 경우)
    public void addCategories(List<VideoCategory> videoCategories) {
        this.Categories.clear();
        if (videoCategories != null) {
            this.Categories.addAll(videoCategories);
        }
    }

    public void updateVideoInfo(VideoUpdateDto updateDto){
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();
    }

    public void updateControlType(Video video) {
        this.controlType = ControlType.CENSORED;
    }
}
