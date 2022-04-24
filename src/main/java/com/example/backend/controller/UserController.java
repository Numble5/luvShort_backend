package com.example.backend.controller;

import com.example.backend.domain.user.dto.SignUpRequestDto;
import com.example.backend.domain.user.dto.SignUpResponseDto;
import com.example.backend.exception.BackendException;
import com.example.backend.security.TokenProvider;
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


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final KakaoApiService kakaoApiService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    // 시작하기 버튼을 눌러 회원가입을 완료한다
    @PostMapping("/signup")
    public SignUpResponseDto signup(@RequestBody SignUpRequestDto signUpRequestDto) throws BackendException {
        return userService.createUser(signUpRequestDto);
    }

    // NOTE: JWT 기반 로그인은 모든 요청마다 사용자를 검증하는 방식으로 구현함 (세션과의 차이점)
    //  Spring Security가 모든 요청마다 서블릿 필터같은 역할을 해서 사용자 검증함
    //  로그인할 때 사용자 검증에 쓸 token과 SignUpRequestDto 생성
    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody SignUpRequestDto signUpRequestDto) {
        /*
        Optional<User> user = userRepository.findByEmail(signUpRequestDto.getEmail());
        if(user.isPresent()) {
            final String token = tokenProvider.createJws(user.get()); // 로그인 할 때 토큰 생성
            final SignUpRequestDto responseSignInDto = SignUpRequestDto.builder()
                    .token(token)
                    .email(signUpRequestDto.getEmail())
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
        */
        return new ResponseEntity<>("{}",HttpStatus.OK);
    }


}
