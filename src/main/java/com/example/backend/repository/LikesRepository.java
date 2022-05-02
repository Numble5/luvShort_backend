package com.example.backend.repository;

import com.example.backend.domain.likes.Likes;
import com.example.backend.domain.user.User;
import com.example.backend.domain.video.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long>{

    @Query("select l from Likes l where l.likeUser = :likeUser and l.likeVideo = :likeVideo")
    Optional<Likes> findLikesByLikeUserAndLikeVideo(User likeUser, Video likeVideo);
}
