package com.example.backend.controller;

import com.example.backend.domain.user.User;
import com.example.backend.domain.user.dto.SignUpRequestDto;
import com.example.backend.domain.user.dto.SignUpResponseDto;
import com.example.backend.domain.user.dto.UserAllResponseDto;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;


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
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // 2-2. User 엔티티 저장한 결과 해당 이메일 갖는 엔티티가 한 개 뿐이라면
        if(userRepository.existsOnlyByEmail(signUpRequestDto.getEmail())){

            // User 엔티티의 List<userInterests>에 잘 저장되었는지 로그 찍어보기 //ok
            User user = userRepository.findByEmail(signUpRequestDto.getEmail()).get();
            user.getUserInterests().forEach(userInterest -> log.info("userInterest : {}", userInterest.toString()));

            // 쿠키 만들고
            ResponseCookie responseCookie = ResponseCookie.from("access_token", tokenProvider.createJws(signUpRequestDto.getEmail()))
                                                        .httpOnly(true)
                                                        .secure(true)
                                                        //.maxAge(7 * 24 * 60 * 60)
                                                        .sameSite("None")
                                                        .build();
            response.setHeader("Set-Cookie", responseCookie.toString());
            return ResponseEntity.created(URI.create("/auth/register-submit"))
                    .body(new SignUpResponseDto(user));
        }
        // 2-3. 한개가 아니라 0개거나 2개 이상이면 내부 서버 오류 응답
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 쿠키정보 보내면 회원정보 리턴(사용자 인증용)
    @GetMapping("/auth/check")
    public ResponseEntity<?> getUserInfoByCookie(HttpServletRequest request){

        String accessToken = jwtAuthenticationFilter.parseCookie(request);
        
        if(accessToken==null){
            return new ResponseEntity<>("no accessToken in cookie", HttpStatus.NO_CONTENT);
        }
        return userService.getUserInfoByJwt(accessToken);


    }

    // 닉네임 중복검사
    @GetMapping("/auth/check/{nickname}")
    public ResponseEntity<?> checkNickname(@PathVariable("nickname") String nickname){
        log.info("/auth/check/{nickname} : {}",nickname);
        if(userRepository.existsByNickname(nickname)){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 사용자 ID -> 사용자 정보 return : 일단 user entity 통채로
    @GetMapping("/user/{email}")
    public ResponseEntity<?> userInfo(@PathVariable("email") String email) {
         UserAllResponseDto responseDto = userService.getUserInfoByEmail(email);

        if(responseDto == null)
            return new ResponseEntity<>(responseDto, HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(responseDto,HttpStatus.OK);
    }

    // 사용자 ID -> 사용자 제거 (회원탈퇴?)
    @DeleteMapping ("/user/{idx}")
    public ResponseEntity<?> deleteUser(@PathVariable("idx") Long idx) {
        Boolean result = userService.deleteUser(idx);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> deleteCookie(HttpServletResponse response){

        response.setHeader("Cookie", null);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /** 새로운 이미지 등록 **/
    @PostMapping("/user/profileImg")
    public ResponseEntity<?> updateProfileImg(@RequestParam("email") String email, MultipartFile profileFile ) {
        return userService.updateProfile(email,profileFile);
    }

    /** 기본 이미지로 변경 **/
    @DeleteMapping("/user/profileImg")
    public ResponseEntity<?> deleteProfileImg(@RequestParam("email") String email) {
        return userService.changeToDefaultImg(email);
    }
}
