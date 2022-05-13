package com.example.backend.controller;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.dto.EditMyProfileDto;
import com.example.backend.domain.user.dto.ProfileResponseDto;
import com.example.backend.exception.ReturnCode;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user/profile")
public class ProfileController {

    private final ProfileService profileService;
    private final UserRepository userRepository;

    // 다른사람 프로필 조회(userEmail을 가진 User가 userIdx를 가진 User의 프로필 조회)
    @GetMapping("/{idx}")
    public ResponseEntity<?> getOtherProfile(@PathVariable("idx") Long userIdx, @RequestParam("userEmail") String userEmail) {

        // userIdx를 가진 User가 없으면 PROFILE_NOT_FOUND 리턴
        Optional<User> profileUser = userRepository.findById(userIdx);
        if(!profileUser.isPresent()){
            return new ResponseEntity<>(ReturnCode.PROFILE_NOT_FOUND, HttpStatus.OK);
        }

        // userEmail을 가진 User가 없으면 USER_NOT_FOUND 리턴
        Optional<User> requestUser = userRepository.findByEmail(userEmail);
        if (!requestUser.isPresent()){
            return new ResponseEntity<>(ReturnCode.USER_NOT_FOUND, HttpStatus.OK);
        }

        return profileService.getOtherProfile(profileUser.get(), requestUser.get());

    }

    @GetMapping
    public ResponseEntity<?> getMyProfile(@RequestParam("userEmail") String userEmail){

        // userIdx를 가진 User가 없으면 PROFILE_NOT_FOUND 리턴
        Optional<User> requestUser = userRepository.findByEmail(userEmail);
        if(!requestUser.isPresent()){
            return new ResponseEntity<>(ReturnCode.USER_NOT_FOUND, HttpStatus.OK);
        }

        return profileService.getMyProfile(requestUser.get());

    }

    @PutMapping
    public ResponseEntity<?> EditMyProfile(@RequestBody EditMyProfileDto editMyProfileDto, @RequestParam("userEmail") String userEmail){
        // userIdx를 가진 User가 없으면 PROFILE_NOT_FOUND 리턴
        Optional<User> requestUser = userRepository.findByEmail(userEmail);
        if(!requestUser.isPresent()){
            return new ResponseEntity<>(ReturnCode.USER_NOT_FOUND, HttpStatus.OK);
        }

        ReturnCode returnCode = profileService.updateMyProfile(requestUser.get(),requestUser.get().getProfile(),editMyProfileDto);
        if (returnCode == ReturnCode.INVALID_INTEREST){
            return new ResponseEntity<>(ReturnCode.INVALID_INTEREST, HttpStatus.OK);
        }
        // 잘 업데이트 되었는지 확인
        Optional<User> responseUser = userRepository.findByEmail(userEmail);

        //log.info("profileImg: {}", responseUser.get().getProfile().getProfileImg());

        //log.info("nickname: {}", responseUser.get().getNickname());
        //log.info("birthday: {}", responseUser.get().getUserInfo().getAge());
        //log.info("gender: {}", responseUser.get().getUserInfo().getGenderType());
        //log.info("city: {}", responseUser.get().getUserInfo().getCity());
        //log.info("nickname: {}", responseUser.get().getUserInfo().getDistrict());
        //log.info("introduce: {}", responseUser.get().getProfile().getIntroduce());

        return new ResponseEntity<>(new ProfileResponseDto(responseUser.get()), HttpStatus.OK);
    }

}
