package com.example.backend.service;

import com.example.backend.domain.likes.Likes;
import com.example.backend.domain.user.User;
import com.example.backend.domain.video.Video;
import com.example.backend.domain.video.dto.ResponseVideoInfo;
import com.example.backend.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class LikeService {

    private final VideoService videoService;
    private final LikesRepository likesRepository;

    // Likes 엔티티 저장
    // 1. Likes 객체 만들기
    // 2. 레포지토리에 저장
    @Transactional
    public void createLikesEntity(User user, Video video) {

        // 1.
        Likes likes = new Likes(user, video);
        user.addLikes(likes);
        // 2.
        likesRepository.save(likes);
    }

    @Transactional
    public void deleteLikesEntity(User user, Likes likes){
        likesRepository.delete(likes);
        user.deleteLikes(likes);
    }

    @Transactional
    public List<ResponseVideoInfo> getAllLikeVideos(User user){

        // Likes -> Video -> ResponseVideoInfo
        return user.getLikesList().stream()
                .map(Likes::getLikeVideo)
                .map(likeVideo -> videoService.makeResVideoInfoWithHeart(likeVideo,true))
                .sorted(Comparator.comparing(ResponseVideoInfo::getCreatedDate).reversed())
                .collect(Collectors.toList());

    }
}
