package com.example.backend.service;


import com.example.backend.domain.likes.Likes;
import com.example.backend.domain.user.User;
import com.example.backend.domain.user.UserInterest;
import com.example.backend.domain.user.dto.OtherProfileResponseDto;
import com.example.backend.domain.user.dto.VideoUploaderDto;
import com.example.backend.domain.video.Video;
import com.example.backend.domain.video.dto.ResponseVideoInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {

    private final VideoService videoService;

    // 무한 참조 방지
    public List<ResponseVideoInfo> getResponseVideoInfoList(User user){
        // Likes -> Video -> ResponseVideoInfo
        return user.getLikesList().stream()
                .map(Likes::getLikeVideo)
                .map(videoService::makeResVideoInfo)
                .collect(Collectors.toList());
    }

    // video_idx로 비교
    public ResponseEntity<?> getOtherProfile(User profileUser, User requestUser) {

        // profileUser(상대방)가 올린 영상과 좋아한 영상의 조회(Dto써서 무한참조 방지)
        //List<ResponseVideoInfo> otherVideos = profileUser.getMyVideos().stream().map(videoService::makeResVideoInfo).collect(Collectors.toList());
        List<ResponseVideoInfo> otherLikesVideoList = getResponseVideoInfoList(profileUser);

        // requestUser(나)가 좋아한 영상의 인덱스 조회하기
        List<ResponseVideoInfo> myLikesVideoList =  getResponseVideoInfoList(requestUser);

        // 내가 상대방을 좋아하는지
        Boolean doILikeOther = false;
        // 상대방이 나를 좋아하는지
        Boolean doesOtherLikesMe = false;
        doesOtherLikesMe = otherLikesVideoList.stream()                             // 상대방이 좋아한 영상의
                                                .map(ResponseVideoInfo::getUploader)                        // 업로더 중
                                                .map(VideoUploaderDto::getUser_idx)
                                                .anyMatch(userIdx -> userIdx.equals(requestUser.getIdx()));    // 내가 있는지

        // 상대방의 영상 중 내가 좋아한 영상이면 Heart=true, 아니면 Heart=false로 ResponseVideoInfo 만들기
        for(ResponseVideoInfo otherLikesVideoDto: otherLikesVideoList){                                 // 상대방의 영상이
            if (myLikesVideoList.contains(otherLikesVideoDto)){                                         // 내가 좋아한 영상에 있으면 FIXME LinkedList-> LinkedHashSet으로 바꿔서 시간복잡도 줄이기
                otherLikesVideoDto.setHeart(true);
                if (!doILikeOther) doILikeOther = true; // 처음 한번만 체크
            }
            else{
                otherLikesVideoDto.setHeart(false);
            }
        }

        Map<String,Object> response = new HashMap<>();

        if(!doesOtherLikesMe && !doILikeOther){
            response.put("isMatched","하트없음");
        }
        else if(!doesOtherLikesMe){
            response.put("isMatched","하트보냄");
        }
        else if (!doILikeOther){
            response.put("isMatched","하트받음");
        }
        else{
            response.put("isMatched","매칭성공");
        }

        response.put("profile", new OtherProfileResponseDto(profileUser));
        response.put("videos", otherLikesVideoList);

        return ResponseEntity.ok().body(response);
    }
}
