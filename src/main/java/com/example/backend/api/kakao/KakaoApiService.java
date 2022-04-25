package com.example.backend.api.kakao;

import com.example.backend.api.kakao.dto.KakaoAccount;
import com.example.backend.api.kakao.dto.KakaoUserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;


    // 카카오 '사용자 정보 가져오기' API에서 accessToken으로 정보 받아오기
    // cf) 카카오 '사용자 정보 가져오기' API https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info
    /**
     * // 요청
     * GET/POST /v2/user/me HTTP/1.1
     * Host: kapi.kakao.com
     * Authorization: Bearer ${ACCESS_TOKEN}
     * Content-type: application/x-www-form-urlencoded;charset=utf-8
     *
     * // 응답
     * Response: 성공, 사용자가 닉네임만 동의한 경우
     * HTTP/1.1 200 OK
     * {
     *     "id":123456789,
     *     "kakao_account": {
     *         "profile_nickname_needs_agreement": false,
     *         "profile": {
     *             "nickname": "홍길동"
     *         }
     *     },
     *     "properties":{
     *         "nickname":"홍길동카톡",
     *         "custom_field1":"23",
     *         "custom_field2":"여",
     *         ...
     *     }
     * }
     */
    public String getUserEmailByAccessToken(String accessToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 요청 만들기
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);

        String url = "https://kapi.kakao.com/v2/user/me";
        try {
            // 응답 받기
            String responseBody = restTemplate.postForEntity(url, request, String.class).getBody();
            KakaoUserInfo userInfo = objectMapper.readValue(responseBody, KakaoUserInfo.class);
            //log.info("userInfo.getId() : {}",userInfo.getId()); //ok
            //log.info("userInfo.getConnectedAt() : {}",userInfo.getConnectedAt()); //ok

            KakaoAccount kakaoAccount = new KakaoAccount(userInfo.getKakaoAccount());
            //log.info("userInfo.getKakaoAccount() : {}",userInfo.getKakaoAccount());
            // 이메일 관련필드
            //log.info("userInfo.getEmailNeedsAgreement() : {}",kakaoAccount.getEmailNeedsAgreement()); //ok
            //log.info("userInfo.getIsEmailValid() : {}",kakaoAccount.getIsEmailValid()); //ok
            //log.info("userInfo.userInfo.getIsEmailVerified() : {}",kakaoAccount.getIsEmailVerified()); //ok
            //log.info("userInfo.getEmail() : {}",kakaoAccount.getEmail()); //ok
            return kakaoAccount.getEmail();

        } catch (RestClientException | JsonProcessingException ex) {
            ex.printStackTrace();
            return "Error";
        }

    }
}
