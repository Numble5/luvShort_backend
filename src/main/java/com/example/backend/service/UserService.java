package com.example.backend.service;

import com.example.backend.config.auth.dto.KakaoUserInfo;
import com.example.backend.domain.user.enums.SocialAccountType;
import com.example.backend.exception.BackendException;
import com.example.backend.exception.ReturnCode;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    public boolean checkAlreadyRegistered(SocialAccountType socialAccountType, Long socialId){
        return userRepository.existsBySocialAccountTypeAndSocialId(socialAccountType, socialId);
    }
    // 인증 서버(카카오, 네이버...)에서 받은 정보로 User 만들기
    public void createUser(KakaoUserInfo kakaoUserInfo){
        /*
        final String email = kakaoUserInfo.getEmail();
        log.info("email from kakaoUserInfo : {}",email);

        // 해당 이메일이 이미 User 테이블에 있으면 Exception 처리
        if(userRepository.existsByEmail(email)){
            log.warn("email already exists");
            throw new BackendException(ReturnCode.USER_EXIST_USING_THIS_EMAIL);
        }
        // 해당 이메일이 User 테이블에 없을 때만 User 엔티티 만들기
        userRepository.save(kakaoUserInfo.toEntity());
        log.info("user saved");

         */
    }
}
