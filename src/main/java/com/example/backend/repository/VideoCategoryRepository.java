package com.example.backend.repository;


import com.example.backend.domain.video.Video;
import com.example.backend.domain.video.VideoCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface VideoCategoryRepository extends JpaRepository<VideoCategory, Long>  {
    // 주어진 카테고리를 가지는 dictinct 한 비디오 목록 return
    @Query("select distinct v.video from VideoCategory v where v.category.CategoryName in (:categories)")
    Optional<List<Video>> findDistinctVideoInCategories(Collection categories);
}
