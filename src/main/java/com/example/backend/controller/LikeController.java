package com.example.backend.controller;

import com.example.backend.domain.likes.Likes;
import com.example.backend.domain.likes.dto.PushHeartButtonResponseDto;
import com.example.backend.domain.user.User;
import com.example.backend.domain.video.Video;
import com.example.backend.exception.ReturnCode;
import com.example.backend.repository.LikesRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VideoRepository;
import com.example.backend.service.LikeService;
import com.example.backend.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final VideoRepository videoRepository;

    // 비디오 좋아요 누르기
    // 1. 사용자가 없으면 INTERNAL_SERVER_ERROR 리턴
    // 2. 비디오가 없으면 NO_CONTENT 리턴
    // 3. 자신이 올린 비디오를 좋아요 하려고 하면 BAD_REQUEST 리턴하기
    // 4. likes 엔티티 가져오기
    // 5-1. likes 엔티티 없으면 likeService의 createLikesEntity 실행
    // 5-2. likes 엔티티 있으면 likeService의 deleteLikesEntity 실행
    @PostMapping("/hearts/{video_idx}")
    public ResponseEntity<?> pushHeartButton(@PathVariable("video_idx") Long videoIdx, @RequestParam("userEmail") String userEmail){

        Optional<Video> video = videoRepository.findById(videoIdx);

        // 사용자가 없으면 잘못된 요청이라고 리턴
        Optional<User> user = userRepository.findByEmail(userEmail);
        // 1.
        if (!user.isPresent()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 2.
        if (!video.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        // 3.
        if(video.get().getUploader().equals(user)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        User userEntity = user.get();
        Video videoEntity = video.get();
        // 4.
        Optional<Likes> likeEntity = likesRepository.findLikesByLikeUserAndLikeVideo(userEntity, videoEntity);
        if (!likeEntity.isPresent()){
            // 5-1.
            likeService.createLikesEntity(userEntity, videoEntity);
            Likes entity = likesRepository.findLikesByLikeUserAndLikeVideo(userEntity, videoEntity).get();
            return new ResponseEntity<>(new PushHeartButtonResponseDto(entity), HttpStatus.CREATED);
        }
        else{
            // 5-2.
            likeService.deleteLikesEntity(likeEntity.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping("/hearts")
    public ResponseEntity<?> getHeartsVideoList(@RequestParam("userEmail") String userEmail) {

        // 사용자가 없으면 잘못된 요청이라고 리턴
        Optional<User> user = userRepository.findByEmail(userEmail);
        if (!user.isPresent()){
            return new ResponseEntity<>(ReturnCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<>(likeService.getAllLikeVideos(user.get()), HttpStatus.OK);
        }
    }
}
