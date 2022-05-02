package com.example.backend.domain.likes;

import com.example.backend.domain.BaseEntity;
import com.example.backend.domain.user.Interest;
import com.example.backend.domain.user.User;
import com.example.backend.domain.video.Video;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table
public class Likes extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_idx")
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User likeUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_idx")
    private Video likeVideo;

    public Likes(User user, Video video) {
        this.likeUser = user;
        this.likeVideo = video;
    }


}
