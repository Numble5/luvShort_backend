package com.example.backend.service;


import com.example.backend.domain.likes.Likes;
import com.example.backend.domain.user.User;
import com.example.backend.domain.user.dto.OtherProfileResponseDto;
import com.example.backend.domain.video.Video;
import com.example.backend.domain.video.dto.ResponseVideoInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {

    private final VideoService videoService;

    public List<Video> getAllLikeVideos(User user){
        // Likes -> Video
        return user.getLikesList().stream()
                .map(Likes::getLikeVideo)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> getOtherProfile(User profileUser, User requestUser) {

        // profileUser(상대방)가 올린 영상과 좋아한 영상 조회하기
        List<Video> otherVideos = profileUser.getMyVideos(); // 데이터 뿌려줘야함
        List<Video> otherLikesVideoList = getAllLikeVideos(profileUser);

        // requestUser(나)가 올린 영상과 좋아한 영상 조회하기
        // List<Video> myVideos = requestUser.getMyVideos(); // 필요없음
        List<Video> myLikesVideoList =  getAllLikeVideos(requestUser);


        // 상대방이 나를 좋아하는지
        Boolean doesOtherLikesMe = otherLikesVideoList.stream()                             // 상대방이 좋아한 영상의
                                            .map(Video::getUploader)                        // 업로더 중
                                            .anyMatch(user -> user.equals(requestUser));    // 내가 있는지

        // 내가 상대방을 좋아하는지
        Boolean doILikeOther = false;

        // 상대방의 영상 중 내가 좋아한 영상이면 Heart=true, 아니면 Heart=false로 ResponseVideoInfo 만들기
        List<ResponseVideoInfo> responseVideoInfoList = new LinkedList<>();
        for(Video otherVideo: otherVideos){                 // 상대방의 영상이
            if (myLikesVideoList.contains(otherVideo)){     // 내가 좋아한 영상에 있으면 FIXME LinkedList-> LinkedHashSet으로 바꿔서 시간복잡도 줄이기
                responseVideoInfoList.add(videoService.makeResVideoInfoWithHeart(otherVideo,true)); //true
                if (!doILikeOther) doILikeOther = true; // 처음 한번만 체크
            }
            else{
                responseVideoInfoList.add(videoService.makeResVideoInfoWithHeart(otherVideo,false));
            }
        }

        String isMatched = "";
        if(!doesOtherLikesMe && !doILikeOther){
            isMatched = "하트없음";
        }
        else if(!doesOtherLikesMe){
            isMatched = "하트보냄";
        }
        else if (!doILikeOther){
            isMatched = "하트받음";
        }
        else{
            isMatched = "매칭성공";
        }
        return new ResponseEntity<>(responseVideoInfoList, HttpStatus.OK);
        //return new ResponseEntity<>(new OtherProfileResponseDto(profileUser, isMatched, responseVideoInfoList), HttpStatus.OK);
    }
}
