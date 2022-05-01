package com.example.backend.service;

import com.example.backend.domain.likes.Likes;
import com.example.backend.domain.user.User;
import com.example.backend.domain.video.Video;
import com.example.backend.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeService {

    private final LikesRepository likesRepository;

    // Likes 엔티티 저장
    // 1. Likes 객체 만들기
    // 2. 레포지토리에 저장
    @Transactional
    public void createLikesEntity(User user, Video video) {

        // 1.
        Likes likes = new Likes(user, video);
        // 2.
        likesRepository.save(likes);
    }

    @Transactional
    public void deleteLikesEntity(Likes likes){
        likesRepository.delete(likes);
    }
}
