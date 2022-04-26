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



@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // 시작하기 버튼을 눌러 회원가입을 완료한다
    // 1. signUpRequestDto으로 User 엔티티를 만든다.
    // 2. 엔티티 잘 생성되었는지 확인
    @PostMapping("/auth/register-submit")
    public ResponseEntity<?> signup(HttpServletResponse response, @RequestBody SignUpRequestDto signUpRequestDto) throws BackendException {

        // 1
        ReturnCode returnCode = userService.createUser(signUpRequestDto);

        // 2-1. 이메일이 이미 있어서 서비스단에서 User 엔티티 만들기 실패했으면 잘못된 요청보냈다고 응답하기
        if(returnCode != ReturnCode.SUCCESS){
            return new ResponseEntity<>(new Message(ReturnCode.USER_EXIST_USING_THIS_EMAIL, null), HttpStatus.OK);
        }
        // 2-2. User 엔티티 저장한 결과 해당 이메일 갖는 엔티티가 한 개 뿐이라면
        if(userRepository.existsOnlyByEmail(signUpRequestDto.getEmail())){

            // User 엔티티의 List<userInterests>에 잘 저장되었는지 로그 찍어보기 //ok
            User user = userRepository.findByEmail(signUpRequestDto.getEmail()).get();
            user.getUserInterests().forEach(userInterest -> log.info("userInterest : {}", userInterest.toString()));

            // 쿠키 만들고
            Cookie cookie = new Cookie("access_token", tokenProvider.createJws(signUpRequestDto.getEmail()));
            cookie.setMaxAge(7 * 24 * 60 * 60);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);

            // 응답 보내기
            response.addCookie(cookie);
            return new ResponseEntity<>(new Message(ReturnCode.SUCCESS, cookie.toString()), HttpStatus.OK);
        }
        // 2-3. 한개가 아니라 0개거나 2개 이상이면 내부 서버 오류 응답
        return new ResponseEntity<>(new Message(ReturnCode.INTERNAL_SERVER_ERROR, null), HttpStatus.OK);
    }

    // 쿠키정보 보내면 회원정보 리턴(사용자 인증용)
    @GetMapping("/auth/check")
    public ResponseEntity<?> getUserInfoByCookie(HttpServletRequest request){

        String accessToken = jwtAuthenticationFilter.parseCookie(request);
        if(accessToken==null){
            return new ResponseEntity<>(new Message(ReturnCode.NO_COOKIE, null), HttpStatus.OK);
        }
        return userService.getUserInfoByJwt(accessToken);
    }

    // 닉네임 중복검사
    @GetMapping("/auth/check/{nickname}")
    public ResponseEntity<?> checkNickname(@PathVariable("nickname") String nickname){
        log.info("/auth/check/{nickname} : {}",nickname);
        if(userRepository.existsByNickname(nickname)){
            return new ResponseEntity<>(new Message(ReturnCode.USER_EXIST_USING_THIS_NICKNAME, null), HttpStatus.OK);
        }
        return new ResponseEntity<>(new Message(ReturnCode.SUCCESS, nickname), HttpStatus.OK);
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
