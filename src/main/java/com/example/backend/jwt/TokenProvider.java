package com.example.backend.jwt;

import com.example.backend.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

// https://tunaguy.tistory.com/entry/Spring-Boot%EC%97%90%EC%84%9C-JWT-%EC%83%9D%EC%84%B1-%EB%B0%8F-%EA%B2%80%EC%A6%9D-%EA%B0%84%EB%8B%A8%ED%9E%88-%EA%B5%AC%ED%98%84?category=926842
@Service
public class TokenProvider {
    // 최소 32자리(256bit)
    private final String RAW_SECRET_KEY = "anstlflxltmxhflwpdltmsenpqxhzmstodtjdrjawmd20210118wpdlejqmfdbxl";

    // 소셜 이메일로 JWT 생성
    public String createJws(String email) {

        // 기한 지금으로부터 1일로 설정
        Date expiryDate = Date.from(
                            Instant.now()
                            .plus(1, ChronoUnit.DAYS));

        Key key = Keys.hmacShaKeyFor(RAW_SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        String jws = Jwts.builder()
                .setSubject(email) // 사용자의 이메일 정보를 payload에 넣기
                .signWith(key)
                .setExpiration(expiryDate)
                .compact();

        return jws;
    }

    public boolean isValid(String jws, User user) {
        Key key = Keys.hmacShaKeyFor(RAW_SECRET_KEY.getBytes(StandardCharsets.UTF_8));

        try {
            Jws<Claims> parsed = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jws);
            return parsed.getBody().getSubject().equals(user.getEmail());
        } catch (JwtException e) {
            return false;
        }
    }

    public String getEmailfromJwt(String jws){
        Key key = Keys.hmacShaKeyFor(RAW_SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        try {
            Jws<Claims> parsed = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jws);
            return parsed.getBody().getSubject();
        } catch (JwtException e) {
            return "forged";
        }

    }
}
