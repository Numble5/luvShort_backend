package com.example.backend.api.kakao;

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

    @PostMapping
    public ResponseEntity<?> kakaoLogin(@RequestBody JSONObject jsonObject){
        log.info("accessToken: {}", jsonObject); //ok accessToken: {"access_token":"rLdYAqQVdVdjbk29pac8ZQ3PHL-cxdM_yy1ISAo9dJgAAAGAUQ9Phw"}
        String accessToken = jsonObject.getAsString("access_token");// value만 추출해야함
        ResponseEntity<?> response = kakaoApiService.getUserByAccessToken(accessToken);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}
