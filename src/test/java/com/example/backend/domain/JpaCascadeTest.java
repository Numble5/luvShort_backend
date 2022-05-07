package com.example.backend.domain;


import com.amazonaws.services.kms.model.NotFoundException;
import com.example.backend.domain.likes.Likes;
import com.example.backend.domain.user.Interest;
import com.example.backend.domain.user.Profile;
import com.example.backend.domain.user.User;
import com.example.backend.domain.user.UserInterest;
import com.example.backend.domain.video.Video;
import com.example.backend.repository.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JpaCascadeTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    LikesRepository likesRepository;

    @Autowired
    UserInterestRepository userInterestRepository;

    @Autowired
    InterestRepository interestRepository;
    @DisplayName("부모(User) 삭제하는 경우 비디오, interest 삭제여부")
    @Test
    public void orphanRemoval_True_InCaseOfUserRemoval() {
        //given

        // 사용자 정보
        User user = User.builder()
                .email("test")
                .nickname("test")
                .build();


        userRepository.save(user);

        Video video = Video.builder()
                .videoUrl("test url")
                .uploader(user)
                .build();

        user.addMyVideo(video);
        videoRepository.save(video);

        // 관심사
//        Interest interest= new Interest("test1");
//        UserInterest userInterest = new UserInterest(user,interest);
        //user.addInterests((List<UserInterest>) userInterest);

//        interestRepository.save(interest);
//        userInterestRepository.save(userInterest);
        // 좋아요
//        Likes likes = new Likes(user,video);
//        likesRepository.save(likes);



        //when
        User testUser = userRepository.findByEmail("test")
                .orElseThrow(() -> new NotFoundException("no"));
        userRepository.delete(testUser);


        //then
        Optional<Video> videos = videoRepository.findByUploader(testUser);
//        Optional<Likes> likes1 = likesRepository.findLikesByLikeUserAndLikeVideo(user,video);
//        Optional<UserInterest> userInterest1 = userInterestRepository.findById(userInterest.getIdx());


        Assert.assertFalse(videos.isPresent());
//        Assert.assertFalse(likes1.isPresent());
//        Assert.assertFalse(userInterest1.isPresent());








    }
}
