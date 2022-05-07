package com.example.backend.controller;

import com.example.backend.domain.video.dto.VideoUploadDto;
import com.example.backend.service.S3Service;
import com.example.backend.domain.dto.Message;
import com.example.backend.domain.video.dto.ResponseVideoInfo;
import com.example.backend.domain.video.dto.VideoFilterRequest;
import com.example.backend.exception.ReturnCode;
import com.example.backend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value ="/api")
public class VideoController {

    private final VideoService videoService;
    private final S3Service s3Service;

    @RequestMapping(value="/videos",method = RequestMethod.GET)
    public ResponseEntity<?> videoList() {
        return new ResponseEntity<>(videoService.getAllVideo(), HttpStatus.OK);
    }

    /** 비디오 목록 페이지네이션**/
    @GetMapping("/videos/paging")
    public ResponseEntity<?> getPagingVideoList(@RequestParam("lastVideoIdx") Long lastVideoId, @RequestParam("size") int size) {
        List<ResponseVideoInfo> videoInfos = videoService.fetchVideoPagesBy(lastVideoId,size);
        return new ResponseEntity<>(videoInfos,HttpStatus.OK);
    }


    @PostMapping(value="/videos/filter")
    public ResponseEntity<?> filteredVideoList(@RequestBody VideoFilterRequest request) {
        List<ResponseVideoInfo> filtered = videoService.filteringVideo(request);
        return new ResponseEntity<>(filtered,HttpStatus.OK);
    }

    /** 비디오 업로드 **/
    @PostMapping("/videos/upload/new")
    public ResponseEntity<?> uploadFileAndInfo(@RequestPart(value = "info") VideoUploadDto requestInfo, MultipartFile videoFile, MultipartFile thumbFile) throws IOException {

        String videoPath = videoFile.isEmpty() ?  "" : s3Service.upload(videoFile,"short-video");
        String thumbPath = thumbFile.isEmpty() ? "": s3Service.upload(thumbFile,"video-thumbnail");
        requestInfo.setVideoUrl(videoPath);
        requestInfo.setThumbUrl(thumbPath);
        return videoService.saveVideo(requestInfo);
    }

}
