package com.example.backend.domain;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.dto.SignUpRequestDto;
import com.example.backend.domain.user.embedded.UserInfo;
import com.example.backend.domain.user.enums.GenderType;
import com.example.backend.domain.video.Video;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VideoRepository;
import com.example.backend.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
//@DataJpaTest
//@ContextConfiguration(classes = BackendApplication.class)
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // EmbeddedDatabase가 아니라 실제 DB를 사용하도록 설정
public class JpaTestApplicationTests {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VideoRepository videoRepository;

    @Autowired
    UserService userService;

    /*
    @After
    public void cleanup() {
        userRepository.deleteAll();
        videoRepository.deleteAll();
    }
     */

    @Test
    public void contextLoads(){

        User user = User
                .builder()
                .email("syhan97@naver.com")
                .userInfo(
                        UserInfo
                                .builder()
                                .age(26)
                                .city("서울")
                                .genderType(GenderType.FEMALE)
                                .build()
                )
        .build();
        userRepository.save(user);

        // 양방향 관계 설정
        Video video = Video.builder().title("제목").uploader(user).build(); // video의 uploader를 user로 설정
        user.addMyVideo(video); // user의 myVideos에 video 추가

        // Video 엔티티 저장
        videoRepository.save(video);

    }

    /* FAIL
    @Test
    public void User엔티티저장할때_UserInterest도_저장되는지_확인(){
        List<String> selectedInterests = new LinkedList<>();
        selectedInterests.add("쇼핑");
        selectedInterests.add("스포츠");
        SignUpRequestDto signUpRequestDto = SignUpRequestDto.builder()
                .email("syhan97@naver.com")
                .nickname("한승윤")
                .birthday("19970821")
                .gender("FEMALE")
                .city("서울")
                .district("용산구")
                .SelectedInterests(selectedInterests)
                        .build();

        // User 엔티티 저장하기
        userService.createUser(signUpRequestDto);

        // FAIL query did not return a unique result: 2
        User user = userRepository.findByEmail("syhan97@naver.com").get();

        user.getUserInterests().forEach(userInterest -> System.out.println(userInterest.getInterest().getInterestName()));

    }
     */

    //  @Test
//    public void videoSave(){
//
//        Video video = Video.builder()
//                .title("워녕")
//                .content("나르시시스트")
//                .hits(0L)
//                .uploader(userRepository.getById(1L))
//                .videoType(VideoType.EMBED)
//                .build();
//
//        videoRepository.save(video);
//
//    }


}
