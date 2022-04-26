package com.example.backend.domain.user;

import com.example.backend.domain.BaseEntity;
import com.example.backend.domain.user.embedded.UserInfo;
import com.example.backend.domain.user.enums.RoleType;
import com.example.backend.domain.user.enums.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table
public class Interest extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interest_idx")
    private Long idx;

    private String interestName;

    @Builder
    public Interest(String interestName){
        this.interestName = interestName;
    }

}
