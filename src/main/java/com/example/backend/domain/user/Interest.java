package com.example.backend.domain.user;

import com.example.backend.domain.BaseEntity;
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

    private String InterestName;
}
