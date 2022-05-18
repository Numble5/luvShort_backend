package com.example.backend.api.kakao;

import com.example.backend.api.kakao.dto.RedirectUrlResponse;
import com.example.backend.domain.user.dto.SignUpResponseDto;
import com.example.backend.exception.ReturnCode;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.TokenProvider;
import com.example.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;
    private final TokenProvider tokenProvider;
    private final UserService userService;

    // 1. 프론트에서 받은 accessToken으로 사용자 정보를 받아 KakaoAccount에 저장하고 이메일을 리턴
    // 2. 이메일을 받아오지 못하면 에러 리턴
    // 3. luvShort만의 jwt 만들기
    // 4. 회원가입 또는 로그인 진행
    @PostMapping("/api/auth/kakao-login")
    public ResponseEntity<?> kakaoLogin(@RequestBody JSONObject jsonObject, HttpServletResponse response) throws IOException {

        log.info("accessToken: {}",jsonObject ); //ok accessToken: {"access_token":"rLdYAqQVdVdjbk29pac8ZQ3PHL-cxdM_yy1ISAo9dJgAAAGAUQ9Phw"}

        String accessToken = jsonObject.getAsString("access_token");// value만 추출해야함

        // accessToken 못받으면 에러 처리
        if (accessToken==null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        // 1.
        String email = kakaoApiService.getUserEmailByAccessToken(accessToken);

        // 2.
        if (email.equals("Error")){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // 3. (회원가입, 로그인 상관없이)
        String jwt = tokenProvider.createJws(email); // ok
        log.info("jwt: {}",jwt);

        // 5.
        // 이미 회원가입을 했으면 쿠키 만들고 메인화면으로 이동
        // lastLoginDate 업데이트는 alreadySignUp에서 실행
        if(userService.alreadySignUp(email)){

            // 쿠키 설정
            ResponseCookie responseCookie = ResponseCookie.from("access_token", jwt)
                    //.path("/")
                    .httpOnly(true)
                    //.domain("https://luvshort.netlify.app/")
                    .secure(true)
                    .maxAge(7 * 24 * 60 * 60)
                    .sameSite("None")
                    .build();
            response.setHeader("Set-Cookie", responseCookie.toString());

            return new ResponseEntity<>(new RedirectUrlResponse("/", email), HttpStatus.OK);

        }
        // 회원가입을 하지 않았으면 step1으로 이동
        else{
            return new ResponseEntity<>(new RedirectUrlResponse("/step1", email), HttpStatus.OK);
        }


    }
}
