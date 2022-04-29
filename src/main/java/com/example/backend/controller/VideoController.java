package com.example.backend.controller;

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

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value ="/api")
public class VideoController {

    private final VideoService videoService;

    @RequestMapping(value="/videos",method = RequestMethod.GET)
    public ResponseEntity<Message> videoList() {
        return new ResponseEntity<>(new Message(ReturnCode.SUCCESS,videoService.getAllVideo()), HttpStatus.OK);
    }

    @GetMapping(value="/videos/filter")
    public ResponseEntity<Message> filteredVideoList(@RequestBody VideoFilterRequest request) {
        List<ResponseVideoInfo> filtered = videoService.filteringVideo(request);
        return new ResponseEntity<>(new Message(ReturnCode.SUCCESS,filtered),HttpStatus.OK);
    }

    /** 비디오 업로드 **/
//    @PostMapping("/api/videos/upload/new")
//    public ResponseEntity<Message> uploadFileAndInfo(@RequestBody VideoUploadDto requestInfo)
//    {
//        videoService.save(requestInfo);
//        return null;
//    }
}
