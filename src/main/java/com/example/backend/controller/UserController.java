package com.example.backend.controller;

import com.example.backend.config.auth.dto.KakaoAccessToken;
import com.example.backend.config.auth.dto.KakaoUserInfoResponseDto;
import com.example.backend.domain.user.User;
import com.example.backend.domain.user.enums.SocialAccountType;
import com.example.backend.dto.ResponseDTO;
import com.example.backend.dto.SignInDto;
import com.example.backend.exception.BackendException;
import com.example.backend.jwt.TokenProvider;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import com.example.backend.api.kakao.KakaoApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final KakaoApiService kakaoApiService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    // 1. 프론트에서 보내준  access_token을 받아서
    // 2. 카카오 '토큰 정보 보기' API에서 access_token으로'회원번호' 받아오기
    // 3. 회원번호로 이미 가입된 회원인지 확인
    // 4. 해당 '회원번호'를 가진 User가 없으면 회원가입 처리를 하고
    // 5. 있으면 로그인 처리
    // cf) 카카오 '토큰 정보 보기' API https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#get-token-info
    @PostMapping("/")
    public ResponseEntity<?> isSignUp(@RequestBody KakaoAccessToken accessToken) throws BackendException { // 1. POST 요청
        /*
        Long socialId = kakaoApiService.accessTokenInfo(accessToken.getAccessToken()); //2.

        // FIXME: 일단 카카오만
        if (userService.checkAlreadyRegistered(SocialAccountType.KAKAO, socialId)){  // 3. 이미 회원가입이 되어 있으면
            // 로그인 처리
            log.info("로그인해야 합니다");
        }
        else{
            log.info("회원가입해야 합니다");
        }
         */
        return new ResponseEntity<>("{}", HttpStatus.CREATED);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody KakaoAccessToken requestDto) throws BackendException {

        return new ResponseEntity<>("{}", HttpStatus.OK);

    }


    // NOTE: JWT 기반 로그인은 모든 요청마다 사용자를 검증하는 방식으로 구현함 (세션과의 차이점)
    //  Spring Security가 모든 요청마다 서블릿 필터같은 역할을 해서 사용자 검증함
    //  로그인할 때 사용자 검증에 쓸 token과 SignInDto 생성
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody SignInDto signInDto) {
        Optional<User> user = userRepository.findByEmail(signInDto.getEmail());

        if(user.isPresent()) {
            final String token = tokenProvider.createJws(user.get()); // 로그인 할 때 토큰 생성
            final SignInDto responseSignInDto = SignInDto.builder()
                    .token(token)
                    .email(signInDto.getEmail())
                    .build();
            return ResponseEntity.ok().body(responseSignInDto);
        } else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed.")
                    .build();
            return ResponseEntity
                    .badRequest()
                    .body(responseDTO);
        }
    }


}
