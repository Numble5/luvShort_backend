package com.example.backend.service;


import com.example.backend.domain.likes.Likes;
import com.example.backend.domain.user.Interest;
import com.example.backend.domain.user.Profile;
import com.example.backend.domain.user.User;
import com.example.backend.domain.user.dto.EditMyProfileDto;
import com.example.backend.domain.user.dto.ProfileResponseDto;
import com.example.backend.domain.user.dto.VideoUploaderDto;
import com.example.backend.domain.video.dto.ResponseVideoInfo;
import com.example.backend.exception.ReturnCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProfileService {

    private final VideoService videoService;
    private final UserService userService;

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
        List<ResponseVideoInfo> otherVideos = profileUser.getMyVideos().stream().map(videoService::makeResVideoInfo).collect(Collectors.toList());
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
        for(ResponseVideoInfo video: otherVideos){                                 // 상대방의 영상이
            if (myLikesVideoList.contains(video)){                                         // 내가 좋아한 영상에 있으면 FIXME LinkedList-> LinkedHashSet으로 바꿔서 시간복잡도 줄이기
                video.setHeart(true);
                if (!doILikeOther) doILikeOther = true; // 처음 한번만 체크
            }
            else{
                video.setHeart(false);
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

        response.put("profile", new ProfileResponseDto(profileUser));
        response.put("videos", otherVideos);

        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<?> getMyProfile(User requestUser){
        Map<String,Object> response = new HashMap<>();
        response.put("profile", new ProfileResponseDto(requestUser));
        response.put("videos",requestUser.getMyVideos().stream().map(videoService::makeResVideoInfo).collect(Collectors.toList()));
        return ResponseEntity.ok().body(response);
    }

    @Transactional
    public ReturnCode updateMyProfile(User user, Profile profile, EditMyProfileDto editMyProfileDto){
        user.getUserInterests().clear(); //NOTE: orphanRemoval = true이므로 데이터베이스의 데이터도 삭제
        //프론트에서 받아온 관심사 문자열이 모두 Interest 테이블에 있는지 확인
        List<String> interestInput = editMyProfileDto.getInterests();
        ReturnCode returnCode = userService.saveUserInterest(interestInput, user);
        if (returnCode == ReturnCode.INVALID_INTEREST){
            return returnCode;
        }
        user.updateUser(editMyProfileDto);
        //profile.updateProfile(editMyProfileDto.getProfileImg(), editMyProfileDto.getIntroduce());
        return ReturnCode.SUCCESS;
    }


}
