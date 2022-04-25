package com.example.backend.service;


import com.example.backend.domain.video.Video;
import com.example.backend.domain.video.dto.ResponseVideoInfo;
import com.example.backend.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;

    @Transactional
    public List<ResponseVideoInfo> getAllVideo(){
        List<Video> videoList = videoRepository.findAll(); // 전체 비디오 목록
        List<ResponseVideoInfo> responseVideoInfoList = new ArrayList<>();

        for(Video v: videoList){
           ResponseVideoInfo info = ResponseVideoInfo.builder()
                   .idx(v.getIdx())
                   .content(v.getContent())
                   .title(v.getTitle())
                   .videoType(v.getVideoType())
                   .videoUrl(v.getVideoUrl())
                   .thumbnailUrl(v.getThumbnailUrl())
                   .hits(v.getHits())
                   .createdDate(v.getCreatedDate())
                   .updatedDate(v.getUpdatedDate())
                   .nickname(v.getUploader().getNickname())
                   .profileImgUrl(v.getUploader().getProfile().getProfileImg()) // 임시 -> user entity 수정? profile 수정?
                   .build();
           responseVideoInfoList.add(info);
        }
        return responseVideoInfoList;
    }
}
