package com.example.backend.controller;

import com.example.backend.domain.dto.Message;
import com.example.backend.domain.user.User;
import com.example.backend.domain.user.dto.SignUpRequestDto;
import com.example.backend.domain.user.dto.SignUpResponseDto;
import com.example.backend.domain.user.dto.UserReponseDtoByCookie;
import com.example.backend.domain.user.embedded.UserInfo;
import com.example.backend.exception.BackendException;
import com.example.backend.exception.ReturnCode;
import com.example.backend.security.JwtAuthenticationFilter;
import com.example.backend.security.TokenProvider;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserService;
import com.example.backend.api.kakao.KakaoApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final KakaoApiService kakaoApiService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // 시작하기 버튼을 눌러 회원가입을 완료한다
    // 1. 이때 프론트에서 보내준 이메일로 jwt를 만들어 쿠키를 만들고, 응답에 추가한다.
    // 2. signUpRequestDto으로 User 엔티티를 만든다.
    @PostMapping("/auth/register-submit")
    public SignUpResponseDto signup(HttpServletResponse response, @RequestBody SignUpRequestDto signUpRequestDto) throws BackendException {

        // 1.
        Cookie cookie = new Cookie("access_token", tokenProvider.createJws(signUpRequestDto.getEmail()));
        cookie.setMaxAge(7 * 24 * 60 * 60);
        cookie.setSecure(true);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        // 2.
        return userService.createUser(signUpRequestDto);
    }

    // 쿠키정보 보내면 회원정보 리턴(사용자 인증용)
    @GetMapping("/auth/check")
    public UserReponseDtoByCookie getUserInfoByCookie(HttpServletRequest request){

        // TODO: 쿠키 3.5일 이하로 남았으면 쿠키 다시 보내기
        // Arrays.stream(request.getCookies()).filter(cookie -> cookie.getMaxAge())

        String accessToken = jwtAuthenticationFilter.parseCookie(request);

        log.info("accessToken getUserInfoByCookie: {}",accessToken);
        String email = tokenProvider.getEmailfromJwt(accessToken);
        Optional<User> user = userRepository.findByEmail(email);
        // 엔티티 객체 없으면 null 리턴
        // 있으면 그 객체의 필드로 dto 초기화
        return user.map(UserReponseDtoByCookie::new).orElse(null);
    }

    @GetMapping("/auth/check/{nickname}")
    public Boolean checkNickname(@PathVariable("nickname") String nickname){
        log.info("/auth/check/{nickname} : {}",nickname);
        if(userRepository.existsByNickname(nickname)){
            return true;
        }
        return false;
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

    // 사용자 ID -> 사용자 정보 return : 일단 user entity 통채로
    @GetMapping("/user/{idx}")
    ResponseEntity<Message> userInfo(@PathVariable("idx") Long userId) {
        UserInfo userInfo = userService.getUserInfoById(userId);

        if(userInfo == null)
            return new ResponseEntity<>(new Message(ReturnCode.USER_NOT_FOUND, null), HttpStatus.OK);
        return new ResponseEntity<>(new Message(ReturnCode.SUCCESS, userInfo),HttpStatus.OK);
    }


}
