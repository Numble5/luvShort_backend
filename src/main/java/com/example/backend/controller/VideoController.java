package com.example.backend.controller;

import com.example.backend.domain.dto.Message;
import com.example.backend.exception.ReturnCode;
import com.example.backend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value ="/api")
public class VideoController {

    private final VideoService videoService;

    @RequestMapping(value="/videos",method = RequestMethod.GET)
    Message videoList() {
        return new Message(ReturnCode.SUCCESS,videoService.getAllVideo());
    }
}
