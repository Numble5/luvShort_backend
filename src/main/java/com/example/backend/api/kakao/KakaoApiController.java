package com.example.backend.api.kakao;

import com.example.backend.config.auth.dto.KakaoAccessToken;
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
@RequestMapping("test")
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;

    // "c:\program files (x86)\google\chrome\application\chrome.exe" --disable-web-security --disable-gpu --disable-features=isolateorigins,site-per-process --user-data-dir="c://chromedev"
    @PostMapping
    public ResponseEntity<?> test(@RequestBody KakaoAccessToken kakaoAccessToken){
       log.info("hi");
       log.info("accessToken: {}", kakaoAccessToken);
        /*
        log.info("KakaoAccessToken: {}",kakaoAccessToken);
        log.info("access_token : {}",kakaoAccessToken.getAccess_token());
        log.info("expires_in : {}",kakaoAccessToken.getExpires_in());
        log.info("refresh_token : {}",kakaoAccessToken.getRefresh_token());
        log.info("refresh_token_expires_in : {}",kakaoAccessToken.getRefresh_token_expires_in());
        log.info("scope : {}",kakaoAccessToken.getScope());
        log.info("token_type : {}",kakaoAccessToken.getToken_type());

         */
        return new ResponseEntity<>("{}", HttpStatus.OK);

    }

}
