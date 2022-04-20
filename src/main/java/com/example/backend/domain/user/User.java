package com.example.backend.domain.user;

import com.example.backend.domain.BaseEntity;
import com.example.backend.domain.user.embedded.UserInfo;
import com.example.backend.domain.user.enums.RoleType;
import com.example.backend.domain.user.enums.UserStatus;
import com.example.backend.domain.video.Video;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long idx;

    @Column(nullable = false)
    private String email;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private RoleType roleType; // 각 사용자의 권한(관리자/일반회원)을 관리할 Enum 클래스

    @Enumerated(EnumType.STRING)
    private UserStatus status; // 각 사용자의 권한을 관리할 Enum 클래스

    @Embedded
    UserInfo userInfo;

    @OneToOne
    @JoinColumn(name = "profile_idx")
    private Profile profile;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserInterest> interests = new LinkedList<>();

    @OneToMany(mappedBy = "uploader", fetch = FetchType.LAZY)
    private List<Video> myVideos = new LinkedList<>();

    @Builder
    public User(String email, String nickname, RoleType roleType, UserStatus status, UserInfo userInfo, Profile profile){
        this.email = email;
        this.nickname = nickname;
        this.roleType = roleType;
        this.status = UserStatus.NORMAL; // 처음 등록한 회원은 무조건 정상회원
        this.userInfo = userInfo;
        this.profile = profile;
    }

    public void addMyVideo(Video myVideo){
        //myVideo.setUser(this);
        this.myVideos.add(myVideo);
    }

}
