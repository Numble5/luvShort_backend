package com.example.backend.service;

import com.example.backend.domain.user.Interest;
import com.example.backend.domain.user.Profile;
import com.example.backend.domain.user.User;
import com.example.backend.domain.user.UserInterest;
import com.example.backend.domain.user.dto.SignUpRequestDto;
import com.example.backend.domain.user.dto.SignUpResponseDto;
import com.example.backend.domain.user.dto.UserAllResponseDto;
import com.example.backend.domain.user.embedded.UserInfo;
import com.example.backend.domain.user.dto.UserReponseDtoByCookie;
import com.example.backend.domain.user.enums.GenderType;
import com.example.backend.exception.ReturnCode;
import com.example.backend.repository.InterestRepository;
import com.example.backend.repository.ProfileRepository;
import com.example.backend.repository.UserInterestRepository;
import com.example.backend.security.TokenProvider;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final InterestRepository interestRepository;
    private final UserInterestRepository userInterestRepository;
    private final ProfileRepository profileRepository;

    private final S3Service s3Service;

    // User 엔티티 만들고 레포지토리에 저장
    // 1. 해당 이메일을 갖는 User 엔티티가 이미 있으면(0개 초과이면) 400 에러
    // 2. signUpDto를 User 엔티티로 변환
    // 3. 관심사 추가
    // 4. 레포지토리에 저장
    @Transactional
    public ReturnCode createUser(SignUpRequestDto signUpRequestDto){

        // 1.
        if (userRepository.existsByEmail(signUpRequestDto.getEmail())){
            return ReturnCode.USER_EXIST_USING_THIS_EMAIL;
        }

        // 2.
        User user = signUpRequestDto.toEntity(); // 1.
        log.info("email: {}", user.getEmail());
        log.info("nickname: {}", user.getNickname());
        log.info("age: {}", user.getUserInfo().getAge());
        log.info("gender: {}", user.getUserInfo().getGenderType());
        log.info("city: {}", user.getUserInfo().getCity());
        log.info("district: {}", user.getUserInfo().getDistrict());

        // 3. 관심사 추가
        // 3-1. 프론트에서 받아온 관심사 문자열이 모두 Interest 테이블에 있는지 확인
        List<String> interestStr = signUpRequestDto.getSelectedInterests();
        List<Interest> interests = new LinkedList<>();
        for(String i: interestStr){
            Optional<Interest> interest = interestRepository.findByInterestName(i);
            if (!interest.isPresent()){
                return ReturnCode.INVALID_INTEREST;
            }
            interests.add(interest.get());
        }

        // 3-2. UserInterest 엔티티 저장하고 User에 저장하기 위해 리스트에 추가
        List<UserInterest> userInterests = new LinkedList<>();
        for(Interest interest: interests){
            // userInterest 객체 생성해서 레포지토리에 저장
            UserInterest userInterest = new UserInterest(user,interest);
            userInterestRepository.save(userInterest);
            userInterests.add(userInterest);
        }
        // 양방향 관계 저장
        user.addInterests(userInterests);

        // 4.
        userRepository.save(user);
        return ReturnCode.SUCCESS;

    }

    // 1. 해당 이메일을 갖는 User 엔티티가 없으면 회원가입(step1으로 이동)
    // 2. User 엔티티가 없으면 로그인(메인 페이지로 이동)
    public Boolean alreadySignUp(String email){
        if(userRepository.existsOnlyByEmail(email)){
            return true;
        }
        return false;
    }

    // email -> user 전체 return
    public UserAllResponseDto getUserInfoByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()) return null;
        return new UserAllResponseDto(user.get());
    }

    public ResponseEntity<?> getUserInfoByJwt(String accessToken){
        String email = tokenProvider.getEmailfromJwt(accessToken);
        if(email.equals("forged")){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userRepository.findByEmail(email);
        // 엔티티 객체 없으면 null 리턴
        if (!user.isPresent()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        // 엔티티 객체 있으면 유저정보 리턴
        else{
            List<UserInterest> userInterests = userInterestRepository.findUserInterestsByUser(user.get());
            List<String> interestStr = new LinkedList<>();
            for(UserInterest userInterest: userInterests){
                interestStr.add(userInterest.getInterest().getInterestName());
            }
            return new ResponseEntity<>(new UserReponseDtoByCookie(user.get(),interestStr), HttpStatus.OK);
        }
    }


    public Boolean checkDefaultImg(String imgUrl) {
        String defaultM =  "https://numble-luvshort.s3.ap-northeast-2.amazonaws.com/profile-image/profile-m.jpeg";
        String defaultW =  "https://numble-luvshort.s3.ap-northeast-2.amazonaws.com/profile-image/profile-w.jpeg";

        if(!imgUrl.equals(defaultW) && !imgUrl.equals(defaultM)) return Boolean.FALSE;
        else return Boolean.TRUE; // 기본이미지일 경우
    }
    @Transactional
    public Boolean deleteUser(Long idx) {
        User user = userRepository.findById(idx)
                .orElseThrow(()-> new NoSuchElementException("해당 사용자가 존재하지 않습니다."));

        // 이미지파일삭제 -> 기본 이미지 아닌 경우
        if(user.getProfile() != null && !checkDefaultImg(user.getProfile().getProfileImg())) {
            s3Service.delete(user.getProfile().getProfileImg(),"profile-image");
        }

        userRepository.delete(user); //cascade - video, VideoCategory, userInterest,Like 다 삭제?

        return true;

    }

    @Transactional
    public ResponseEntity<?> updateProfile(String email , MultipartFile file) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 존재하지 않습니다."));


        Profile profile = user.getProfile();

        if(!checkDefaultImg(profile.getProfileImg())){
            // 기본 이미지 아닌 경우 -> 기존 이미지 삭제
            String imgUrl = profile.getProfileImg();
            s3Service.delete(imgUrl,"profile-image");
        }
        // 프로필 변경 사항 저장
        profile.updateImg(s3Service.upload(file,"profile-image"));
        profileRepository.save(profile);
        return new ResponseEntity<>(profile.getProfileImg(),HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> changeToDefaultImg(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("해당 사용자가 존재하지 않습니다."));

        Profile profile = user.getProfile();
        String defaultM =  "https://numble-luvshort.s3.ap-northeast-2.amazonaws.com/profile-image/profile-m.jpeg";
        String defaultW =  "https://numble-luvshort.s3.ap-northeast-2.amazonaws.com/profile-image/profile-w.jpeg";

        if(!checkDefaultImg(profile.getProfileImg())){
            // 기본 이미지 아닌 경우 -> 기존 이미지 삭제
            String imgUrl = profile.getProfileImg();
            s3Service.delete(imgUrl,"profile-image");

            // 사용자 성별 확인 후 기본 이미지
            profile.updateImg(user.getUserInfo().getGenderType() == GenderType.MALE ?
                    defaultM : defaultW);

            profileRepository.save(profile);
        }

        return new ResponseEntity<>(profile.getProfileImg(),HttpStatus.OK);

    }
}
