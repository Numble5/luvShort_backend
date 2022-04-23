package com.example.backend.controller;

import com.example.backend.domain.video.dto.ResponseVideoInfo;
import com.example.backend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value ="/video")
public class VideoController {

    private final VideoService videoService;

    @RequestMapping(value="/category",method = RequestMethod.GET)
    List<ResponseVideoInfo> videoList() {
        return videoService.getAllVideo();
    }
}
