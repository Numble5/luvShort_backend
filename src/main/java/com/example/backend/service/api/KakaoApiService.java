package com.example.backend.service.api;

import com.example.backend.config.dto.KakaoAccessTokenResponseDto;
import com.example.backend.exception.BackendException;
import com.example.backend.exception.ReturnCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public KakaoAccessTokenResponseDto getKakaoAccount(String targetIdType, long targetId){

        // 요청 관련 API 문서 https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-request
        // 응답 관련 API 문서 https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#req-user-info-response

        /**
         * 요청
         */
        // 1. Header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 2. Parameter
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("target_id_type",targetIdType); //string 회원번호 종류, user_id로 고정
        params.add("target_id", String.valueOf(targetId));//long 사용자 정보를 가져올 사용자의 회원번호

        HttpEntity<MultiValueMap<String,String>> request = new HttpEntity<>(params,headers);


        /**
         * 응답
         */
        String url = "https://kapi.kakao.com/v2/user/me";
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            return objectMapper.readValue(response.getBody(), KakaoAccessTokenResponseDto.class);
        } catch (RestClientException | JsonProcessingException ex){
            ex.printStackTrace();
            throw new BackendException(ReturnCode.FAIL_TO_GET_KAKAO_ACCOUNT);
        }

    }

}
