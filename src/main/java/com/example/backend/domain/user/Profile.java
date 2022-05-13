package com.example.backend.domain.user;

import com.example.backend.domain.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table
public class Profile extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_idx")
    private Long idx;

    @Column
    private String profileImg;

    @Column
    private String introduce;

    @Builder
    public Profile(String profileImg, String introduce) {
        this.profileImg = profileImg;
        this.introduce = introduce;
    }

    public void updateImg(String img) {
        this.profileImg = img;
    }
}
