package com.example.backend.service;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.dto.SignUpRequestDto;
import com.example.backend.domain.user.dto.SignUpResponseDto;
import com.example.backend.security.TokenProvider;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    // User 엔티티 만들고 레포지토리에 저장
    // 1. 프론트로부터 받은 jwt로 이메일 디코딩해서 사용자 검증
    // 2. signUpDto를 User 엔티티로 변환
    // 3. 관심사 추가
    // 4. 레포지토리에 저장

    @Transactional
    public SignUpResponseDto createUser(Cookie[] cookies, SignUpRequestDto signUpRequestDto){
        // 1.
        log.info("Cookies: {}", Arrays.toString(cookies));

        // 2.
        User user = signUpRequestDto.toEntity();
        // TODO: 관심사 추가
        userRepository.save(user);
        return SignUpResponseDto.builder()
                .nickname(user.getNickname())
                .status(200).build();


    }


    // 1. 해당 이메일을 갖는 User 엔티티가 없으면 회원가입(step1으로 이동)
    // 2. User 엔티티가 없으면 로그인(메인 페이지로 이동)

    public Boolean alreadySignUp(String email){
        if(userRepository.existsByEmail(email)){
            return true;
        }
        return false;
    }


}
