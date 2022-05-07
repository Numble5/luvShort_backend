package com.example.backend.repository;

import com.example.backend.domain.user.User;
import com.example.backend.domain.video.Video;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findAllBy();
    Page<Video> findByIdxLessThan(Long lastId, Pageable page);
    Video findTop1ByOrderByIdxDesc();

    Optional<Video> findByIdx(Long idx);
    Optional<Video> findByUploader(User uploader);

}
