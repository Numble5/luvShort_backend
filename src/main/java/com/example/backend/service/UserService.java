package com.example.backend.service;

import com.example.backend.domain.user.User;
import com.example.backend.dto.SignUpDto;
import com.example.backend.exception.ReturnCode;
import com.example.backend.jwt.TokenProvider;
import com.example.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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
    public ReturnCode createUser(SignUpDto signUpDto){
        // 1.
        String email = tokenProvider.getEmailfromJwt(signUpDto.getJwt());
        if(email.equals("forged")){
            return ReturnCode.FORGED_EMAIL;
        }
        log.info("email: {}", email);
        // 2.
        User user = signUpDto.toEntity();
        // TODO: 관심사 추가
        userRepository.save(user);
        return ReturnCode.SUCCESS;
    }

    // 1. 해당 이메일을 갖는 User 엔티티가 없으면 회원가입(step1으로 이동)
    // 2. User 엔티티가 없으면 로그인(메인 페이지로 이동)
    public ResponseEntity<?> signUpOrSignin(String email,String jwt){
        // 1. step1으로 이동
        log.info("jwt: {}",jwt);
        if(!userRepository.existsByEmail(email)){
            JSONObject reponseBody = new JSONObject();
            reponseBody.put("jwt", jwt);
            reponseBody.put("redirectUrl", "/step1");
            return ResponseEntity.ok().body(reponseBody);
        }
        // 2. 메인페이지로 이동
        else{
            JSONObject reponseBody = new JSONObject();
            reponseBody.put("jwt", jwt);
            reponseBody.put("redirectUrl", "/");
            return ResponseEntity.ok().body(reponseBody);
        }
    }
}
