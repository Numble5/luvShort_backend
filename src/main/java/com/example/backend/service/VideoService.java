package com.example.backend.service;


import com.example.backend.domain.likes.Likes;
import com.example.backend.domain.user.Interest;
import com.example.backend.domain.user.User;
import com.example.backend.domain.user.UserInterest;
import com.example.backend.domain.user.embedded.UserInfo;
import com.example.backend.domain.user.enums.GenderType;
import com.example.backend.domain.video.Category;
import com.example.backend.domain.video.Video;
import com.example.backend.domain.video.VideoCategory;
import com.example.backend.domain.video.dto.ResponseVideoInfo;
import com.example.backend.domain.video.dto.VideoFilterRequest;
import com.example.backend.domain.video.dto.VideoUpdateDto;
import com.example.backend.domain.video.dto.VideoUploadDto;
import com.example.backend.domain.video.enums.VideoType;
import com.example.backend.exception.ReturnCode;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VideoCategoryRepository;
import com.example.backend.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class VideoService {
    private final VideoRepository videoRepository;
    private final VideoCategoryRepository videoCategoryRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    private final S3Service s3Service;

//    public List<String> getInterestNames(List<UserInterest> userInterests) {
//        List<String> interests = new ArrayList<>();
//        if(userInterests.size() != 0) {
//            for(UserInterest userInterest: userInterests)
//            interests.add(userInterest.getInterest().getInterestName());
//        }
//        return interests;
//    }
    public ResponseVideoInfo makeResVideoInfo(Video v) {
        //List<ResponseVideoInfo> responseVideoInfoList = new ArrayList<>();
        ResponseVideoInfo info = ResponseVideoInfo.builder()
                .idx(v.getIdx())
                .content(v.getContent())
                .title(v.getTitle())
                .videoType(v.getVideoType())
                .videoUrl(v.getVideoUrl())
                .thumbnailUrl(v.getThumbnailUrl())
                .hits(v.getHits())
                .categories(v.getCategories())
                .createdDate(v.getCreatedDate())
                .updatedDate(v.getUpdatedDate())
                .uploader(v.getUploader())
                .build();
        return info;
    }

    public ResponseVideoInfo makeResVideoInfoWithHeart(Video v, Boolean heart) {
        //List<ResponseVideoInfo> responseVideoInfoList = new ArrayList<>();
        ResponseVideoInfo info = ResponseVideoInfo.builder()
                .idx(v.getIdx())
                .content(v.getContent())
                .title(v.getTitle())
                .videoType(v.getVideoType())
                .videoUrl(v.getVideoUrl())
                .thumbnailUrl(v.getThumbnailUrl())
                .hits(v.getHits())
                .categories(v.getCategories())
                .createdDate(v.getCreatedDate())
                .updatedDate(v.getUpdatedDate())
                .uploader(v.getUploader())
                .heart(heart)
                .build();
        return info;
    }

    // user가 좋아한 video 가져오기(likes에서 변환)
    @Transactional
    public List<Video> getLikeVideoByUser(User user){
        return user.getLikesList().stream()
                .map(Likes::getLikeVideo)
                .collect(Collectors.toList());
    }


    @Transactional
    public List<ResponseVideoInfo> getLikeVideosByUserThenMakeDtoList(User user,List<Video> videoList){
        // user가 좋아한 video 가져오기(likes에서 변환)
        List<Video> likeVideoList = user.getLikesList().stream()
                                        .map(Likes::getLikeVideo)
                                        .collect(Collectors.toList());

        // List<Video> -> List<ResponseVideoInfo> 변환
        List<ResponseVideoInfo> dtoList = new ArrayList<>();
        for(Video v: videoList){
            if (likeVideoList.contains(v)){
                dtoList.add(makeResVideoInfoWithHeart(v,true));
            }
            else{
                dtoList.add(makeResVideoInfoWithHeart(v,false));
            }
        }
        return dtoList;
    }

    @Transactional
    public List<ResponseVideoInfo> getAllVideo(User user){
        List<Video> videoList = videoRepository.findAllBy(); // 전체 비디오 목록

        // 반복되는 코드 함수로 뺌
        return getLikeVideosByUserThenMakeDtoList(user,videoList);
    }

    @Transactional
    public ResponseVideoInfo getVideoDto(Long videoIdx, User user) throws Exception{
        Video video = videoRepository.findByIdx(videoIdx)
                .orElseThrow(() -> new IllegalArgumentException("해당 문제가 존재하지 않습니다."));

        // user가 좋아한 video 가져오기(likes에서 변환)
        List<Video> likeVideoList = getLikeVideoByUser(user);

        if (likeVideoList.contains(video)) {
            return makeResVideoInfoWithHeart(video,true);
        }
        else{
            return makeResVideoInfoWithHeart(video,false);
        }

    }
    @Transactional
    public List<ResponseVideoInfo> fetchVideoPagesBy(Long lastVideoId,int size,User user) {

        if(lastVideoId == 0L) { // 첫 요청의 경우 0-> 가장 큰 idx + 1로 최신것 size 갯수만큼
            lastVideoId = videoRepository.findTop1ByOrderByIdxDesc().getIdx() + 1;
        }
        PageRequest pageRequest = PageRequest.of(0,size, Sort.by("idx").descending()); // 페이지네이션 위해, 페이지는 항상 0으로 고정
        Page<Video> fetchedVideo = videoRepository.findByIdxLessThan(lastVideoId,pageRequest); // id 작으면 최신 -> 작은 순 정렬 && 마지막보다 작은것 size 만큼

        // 반복되는 코드 함수로 뺌
        return getLikeVideosByUserThenMakeDtoList(user,fetchedVideo.getContent());

    }
    @Transactional
    public List<ResponseVideoInfo> filteringVideo(VideoFilterRequest request,User user) {
        // return type

        // 1. 카테고리 필터링

        // 1.1 카테고리 선택 항목 null 제외 선택
        Collection<String> categories = new ArrayList<>();
        if(request.getCategory1() != null) categories.add(request.getCategory1());
        if(request.getCategory2() != null) categories.add(request.getCategory2());
        if(request.getCategory3() != null) categories.add(request.getCategory3());

        Optional<List<Video>> filteredByCategory;
        // 1.1 카테고리 선택하지 않았을 경우 전체 video
        filteredByCategory = Optional.of(videoRepository.findAll());
        // 1.2. 카테고리 선택했을 경우 video-category entity 에서 찾기
        if(!categories.isEmpty()) filteredByCategory = videoCategoryRepository.findDistinctVideoInCategories(categories);



        // 2. 성별 / 시 / 구 필터링

        // 2.1 카테고리 필터링 후 비디오 존재하는 경우
        if(filteredByCategory.isPresent()) {
            List<Video> filteredCom = new ArrayList<>();
            // 3. 성별 / 도시 / 구 존재 경우 무조건 필터링
            for(Video v: filteredByCategory.get()) {
                UserInfo info = v.getUploader().getUserInfo();
                // gender null 이거나 gender 일치 시 위치 비교 가능
                if(request.getGender() != null && !request.getGender().equals(info.getGenderType().getGender())) continue;
                //
                if(request.getCity() != null && !request.getCity().equals(info.getCity())) continue; // 시 존재 && 시 다름
                if(request.getCity() != null && request.getCity().equals(info.getCity())) // 시 동일
                {
                    if(request.getDistrict() != null && !request.getDistrict().equals(info.getDistrict())) continue; // 구 존재 && 구 다륾
                } // 시가 다르면 구는 필터링 항목으로 들어가지 않는다.
                filteredCom.add(v);

            }
            // 반복되는 코드 함수로 뺌
            return getLikeVideosByUserThenMakeDtoList(user,filteredCom);
        }
        // 2.2 비디오 존재하지 않는 경우
        else{
            return null;
        }


    }

    @Transactional
    public ResponseEntity<?> saveVideo(VideoUploadDto uploadDto) {
        // uploader 찾기
        Optional<User> user = userRepository.findByEmail(uploadDto.getEmail());

        // uploader 존재하지 않을 경우
        if(!user.isPresent()) return new ResponseEntity<>(ReturnCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);


        // 비디오 생성
        Video video = Video.builder()
                .title(uploadDto.getTitle())
                .content(uploadDto.getContent())
                .hits(0L)
                .thumbnailUrl(uploadDto.getThumbUrl())
                .videoUrl(uploadDto.getVideoUrl())
                .uploader(user.get())
                .videoType(VideoType.valueOf(uploadDto.getVideoType()))
        .build();

        videoRepository.save(video);

        // 2. 사용자 입력 카테고리 존재 여부 확인
        List<VideoCategory> videoCategories = new LinkedList<>();
        List<String> userInput = uploadDto.getCategories();
        for(String c: userInput) {
            Optional<Category> category = categoryRepository.findCategoryByCategoryName(c);

            // 2.1 카테고리 존재하지 않을 경우 -> 존재하지 않는 카테고리 오류
            if(!category.isPresent()) {
                return new ResponseEntity<>(ReturnCode.INVALID_CATEGORY,HttpStatus.BAD_REQUEST);
            }
            // 2.2 카테고리-비디오 관계 저장
            VideoCategory videoCategory = new VideoCategory(video,category.get());
            videoCategoryRepository.save(videoCategory);
            videoCategories.add(videoCategory);
        }

        // 양방향 관계 지정
        video.addCategories(videoCategories);
        videoRepository.save(video);



        return new ResponseEntity<>(makeResVideoInfo(video),HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateVideo(VideoUpdateDto requestInfo) {
        Video video = videoRepository.findByIdx(requestInfo.getIdx())
                .orElseThrow(()-> new NoSuchElementException("해당 파일이 존재하지 않습니다."));

        // 비디오가 속하는 카테고리 관계 모두 삭제
        videoCategoryRepository.deleteByVideo(video);

        // 카테고리 관계 새로 저장
        List<String> userInput = requestInfo.getCategories();
        List<VideoCategory> videoCategories = new LinkedList<>();
        for(String c: userInput){
            Category category = categoryRepository.findCategoryByCategoryName(c)
                    .orElseThrow(()->new NoSuchElementException(ReturnCode.INVALID_INTEREST.getMessage()));
            // 2.2 카테고리-비디오 관계 저장
            VideoCategory videoCategory = new VideoCategory(video,category);
            videoCategoryRepository.save(videoCategory);
            videoCategories.add(videoCategory);
        }
        video.addCategories(videoCategories);

        // 다른 수정 사항 update
        video.updateVideoInfo(requestInfo);
        // entity update
        videoRepository.save(video);
        return new ResponseEntity<>(makeResVideoInfo(video),HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> deleteVideo(Long videoIdx) {
        Video video = videoRepository.findByIdx(videoIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 비디오입니다."));

        // 1. 임베드 파일 경우
        if(video.getVideoType() == VideoType.EMBED){
            videoRepository.delete(video);
            return new ResponseEntity<>("임베드 영상 삭제", HttpStatus.OK);
        }
        // 2. 직접 업로드 경우
        // 2.1) 동영상 파일 삭제
        String videoUrl = video.getVideoUrl();
        s3Service.delete(videoUrl,"short-video");

        // 2.2) 썸네일 삭제
        String thumbUrl = video.getThumbnailUrl();
        if(thumbUrl != "" || !thumbUrl.isEmpty()) {
            s3Service.delete(thumbUrl,"video-thumbnail");
        }

        // 2.3) video record 삭제
        videoRepository.delete(video);
        return new ResponseEntity<>("직접 영상 삭제", HttpStatus.OK);

    }

}
