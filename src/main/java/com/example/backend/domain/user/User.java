package com.example.backend.domain.user;

import com.example.backend.domain.BaseEntity;
import com.example.backend.domain.likes.Likes;
import com.example.backend.domain.user.dto.EditMyProfileDto;
import com.example.backend.domain.user.embedded.UserInfo;
import com.example.backend.domain.user.enums.RoleType;
import com.example.backend.domain.user.enums.UserStatus;
import com.example.backend.domain.video.Video;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long idx;

    //@Column(nullable = false)
    private String email;
    private String nickname;

    @Enumerated(EnumType.STRING)
    private RoleType roleType; // 각 사용자의 권한(관리자/일반회원)을 관리할 Enum 클래스

    @Enumerated(EnumType.STRING)
    private UserStatus status; // 각 사용자의 상태(정상/탈퇴) 관리할 Enum 클래스

    private LocalDateTime lastLoginDate;

    @Embedded
    UserInfo userInfo;

    @OneToOne(cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    @JoinColumn(name = "profile_idx")
    private Profile profile;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private List<UserInterest> userInterests = new LinkedList<>();

    @OneToMany(mappedBy = "uploader", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private List<Video> myVideos = new LinkedList<>();

    @OneToMany(mappedBy = "likeUser", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private List<Likes> likesList = new LinkedList<>();



    @Builder
    public User(String email, String nickname, RoleType roleType, UserStatus status, UserInfo userInfo, Profile profile){
        this.email = email;
        this.nickname = nickname;
        this.roleType = roleType;
        this.status = status;
        this.userInfo = userInfo;
        this.profile = profile;
    }

    public void addMyVideo(Video myVideo){
        //myVideo.setUser(this);
        this.myVideos.add(myVideo);
    }

    public void addLikes(Likes likes){
        this.likesList.add(likes);
    }

    public void deleteLikes(Likes likes){
        this.likesList.remove(likes);
    }

    public void updateLoginDate(){
        this.lastLoginDate = LocalDateTime.now();
    }

    public void updateUser(EditMyProfileDto editMyProfileDto){
        this.nickname = editMyProfileDto.getNickname();
    }


}
