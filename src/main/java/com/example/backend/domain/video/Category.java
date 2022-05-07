package com.example.backend.domain.video;

import com.example.backend.domain.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table
public class Category extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_idx")
    private Long idx;

    private String categoryName;

    @OneToMany(mappedBy = "category")
    List<VideoCategory> videos = new LinkedList<>(); // 해당 카테고리에 속한 영상들

}
