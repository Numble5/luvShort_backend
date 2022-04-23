package com.example.backend.domain;

import com.example.backend.BackendApplication;
import com.example.backend.domain.user.User;
import com.example.backend.domain.user.embedded.UserInfo;
import com.example.backend.domain.user.enums.GenderType;
import com.example.backend.domain.video.Video;
import com.example.backend.domain.video.enums.VideoType;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.VideoRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
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

    /*
    @After
    public void cleanup() {
        userRepository.deleteAll();
        videoRepository.deleteAll();
    }
     */

    @Test
    public void contextLoads(){


        User user = User.builder().email("syhan97@naver.com").userInfo(new UserInfo(26,"서울", GenderType.FEMALE)).build();
        userRepository.save(user);

        // 양방향 관계 설정
        Video video = Video.builder().title("제목").uploader(user).build(); // video의 uploader를 user로 설정
        user.addMyVideo(video); // user의 myVideos에 video 추가

        // Video 엔티티 저장
        videoRepository.save(video);

    }
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
