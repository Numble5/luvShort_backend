package com.example.backend.api.kakao;

import com.example.backend.security.TokenProvider;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth/kakao-login")
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    // 1. 프론트에서 받은 accessToken으로 사용자 정보를 받아 KakaoAccount에 저장하고 이메일을 리턴
    // 2. 이메일을 받아오지 못하면 에러 리턴
    // 3. luvShort만의 jwt 만들기
    // 4. 회원가입 또는 로그인 진행
    @PostMapping
    public ResponseEntity<?> kakaoLogin(@RequestBody JSONObject jsonObject){

        log.info("accessToken: {}", jsonObject); //ok accessToken: {"access_token":"rLdYAqQVdVdjbk29pac8ZQ3PHL-cxdM_yy1ISAo9dJgAAAGAUQ9Phw"}
        String accessToken = jsonObject.getAsString("access_token");// value만 추출해야함
        // 1.
        String email = kakaoApiService.getUserEmailByAccessToken(accessToken);
        // 2.
        if (email.equals("Error")){
            return new ResponseEntity<>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // 3. (회원가입, 로그인 상관없이)
        String jwt = tokenProvider.createJws(email); // ok
        // 4.
        return userService.signUpOrSignin(email,jwt);

    }
}
