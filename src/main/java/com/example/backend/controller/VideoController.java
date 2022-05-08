package com.example.backend.controller;

import com.example.backend.domain.user.User;
import com.example.backend.domain.video.dto.VideoUploadDto;
import com.example.backend.domain.video.enums.VideoType;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.S3Service;
import com.example.backend.domain.video.dto.ResponseVideoInfo;
import com.example.backend.domain.video.dto.VideoFilterRequest;
import com.example.backend.exception.ReturnCode;
import com.example.backend.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value ="/api")
public class VideoController {

    private final VideoService videoService;
    private final S3Service s3Service;
    private final UserRepository userRepository;

    @RequestMapping(value="/videos",method = RequestMethod.GET)
    public ResponseEntity<?> videoList(@RequestParam("userIdx") Long userIdx) {

        // 사용자가 없으면 잘못된 요청이라고 리턴
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent()){
            return new ResponseEntity<>(ReturnCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        else{
            return new ResponseEntity<>(videoService.getAllVideo(user.get()), HttpStatus.OK);
        }
    }

    @GetMapping("/videos/{idx}")
    public ResponseEntity<?> videoDetail(@PathVariable("idx") Long problemIdx, @RequestParam("userIdx") Long userIdx) throws Exception {

        // 사용자가 없으면 잘못된 요청이라고 리턴
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent()){
            return new ResponseEntity<>(ReturnCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(videoService.getVideoDto(problemIdx,user.get()), HttpStatus.OK);
    }

    /** 비디오 목록 페이지네이션**/
    @GetMapping("/videos/paging")
    public ResponseEntity<?> getPagingVideoList(@RequestParam("lastVideoIdx") Long lastVideoId, @RequestParam("size") int size, @RequestParam("userIdx") Long userIdx) {

        // 사용자가 없으면 잘못된 요청이라고 리턴
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent()){
            return new ResponseEntity<>(ReturnCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        List<ResponseVideoInfo> videoInfos = videoService.fetchVideoPagesBy(lastVideoId,size,user.get());
        return new ResponseEntity<>(videoInfos,HttpStatus.OK);
    }


    @PostMapping(value="/videos/filter")
    public ResponseEntity<?> filteredVideoList(@RequestBody VideoFilterRequest request, @RequestParam("userIdx") Long userIdx) {

        // 사용자가 없으면 잘못된 요청이라고 리턴
        Optional<User> user = userRepository.findById(userIdx);
        if (!user.isPresent()){
            return new ResponseEntity<>(ReturnCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        List<ResponseVideoInfo> filtered = videoService.filteringVideo(request,user.get());
        return new ResponseEntity<>(filtered,HttpStatus.OK);
    }

    /** 비디오 업로드 **/

    /** 임베드 영상 업로드 -> 유튜브 영상 주소 저장 **/
    @PostMapping("/videos/upload/embed")
    public ResponseEntity<?> uploadEmbeded(@RequestBody VideoUploadDto requestInfo) throws Exception {
        requestInfo.setVideoType("EMBED");
        return videoService.saveVideo(requestInfo);
    }

    /** 직접 영상 업로드 **/
    @PostMapping("/videos/upload/direct")
    public ResponseEntity<?> uploadFileAndInfo(@RequestPart(value = "info") VideoUploadDto requestInfo, MultipartFile videoFile, MultipartFile thumbFile) throws IOException {

        String videoPath = videoFile.isEmpty() ?  "" : s3Service.upload(videoFile,"short-video");
        String thumbPath = thumbFile.isEmpty() ? "": s3Service.upload(thumbFile,"video-thumbnail");
        requestInfo.setVideoUrl(videoPath);
        requestInfo.setThumbUrl(thumbPath);
        requestInfo.setVideoType("DIRECT"); // 직접 영상 업로드
        return videoService.saveVideo(requestInfo);
    }

}
