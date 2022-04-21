package com.example.backend.controller.api;

import com.example.backend.config.auth.dto.KakaoAccessToken;
import com.example.backend.service.api.KakaoApiService;
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
@RequestMapping("/api/auth/kakao-login")
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;

    // FIXME: 프론트가 /api/auth/kakao-login로 요청한 access_token을 카카오 서버에서 받아야함(redirect URI 등록해야 하는듯)
   @PostMapping
    public ResponseEntity<?> test(@RequestBody KakaoAccessToken kakaoAccessToken){
        log.info("KakaoAccessToken: {}",kakaoAccessToken);
        log.info("access_token : {}",kakaoAccessToken.getAccess_token());
        log.info("expires_in : {}",kakaoAccessToken.getExpires_in());
        log.info("refresh_token : {}",kakaoAccessToken.getRefresh_token());
        log.info("refresh_token_expires_in : {}",kakaoAccessToken.getRefresh_token_expires_in());
        log.info("scope : {}",kakaoAccessToken.getScope());
        log.info("token_type : {}",kakaoAccessToken.getToken_type());
        return new ResponseEntity<>("{}", HttpStatus.OK);

    }

}
