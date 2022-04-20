package com.example.backend.repository;

import com.example.backend.domain.video.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {

}
