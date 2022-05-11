package com.example.backend.controller;

import com.example.backend.domain.user.User;
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

    // 프로필 조회(userEmail을 가진 User가 userIdx를 가진 User의 프로필 조회)
    @GetMapping("/{idx}")
    public ResponseEntity<?> getProfile(@PathVariable("idx") Long userIdx, @RequestParam("userEmail") String userEmail) {

        // userIdx를 가진 User가 없으면 PROFILE_NOT_FOUND 리턴
        Optional<User> profileUser = userRepository.findById(userIdx);
        if(!profileUser.isPresent()){
            return new ResponseEntity<>(ReturnCode.PROFILE_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        // userEmail을 가진 User가 없으면 USER_NOT_FOUND 리턴
        Optional<User> requestUser = userRepository.findByEmail(userEmail);
        if (!requestUser.isPresent()){
            return new ResponseEntity<>(ReturnCode.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
        }

        return profileService.getOtherProfile(profileUser.get(), requestUser.get());

    }

}
