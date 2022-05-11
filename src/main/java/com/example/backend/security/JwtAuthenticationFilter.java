package com.example.backend.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 리퀘스트에서 토큰 가져오기.
            String token = parseCookie(request);
            log.info("Filter is running... token: {}",token);
            // 토큰 검사하기. JWT이므로 인가 서버에 요청 하지 않고도 검증 가능.
            if (token != null && !token.equalsIgnoreCase("null")) {
                // userEmail 가져오기. 위조 된 경우 예외 처리 된다.
                String userEmail = tokenProvider.getEmailfromJwt(token);
                log.info("Authenticated user ID : " + userEmail );
                // 인증 완료; SecurityContextHolder에 등록해야 인증된 사용자라고 생각한다.
                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userEmail, // 인증된 사용자의 정보
                        null, //
                        AuthorityUtils.NO_AUTHORITIES
                );
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication); // 세션에서 계속 사용하기 위해 securityContext에 Authentication 등록
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    public String parseCookie(HttpServletRequest request){
        String cookie = request.getHeader("cookie");

        if (StringUtils.hasText(cookie) && cookie.startsWith("access_token")) {
            String[] cookieInfos = cookie.split(";");
            for(String cookieInfo : cookieInfos){
                log.info("cookieInfo: {}", cookieInfo);

                /*
                cookieInfo: access_token=eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYXZhbm5haDAzMEBkYXVtLm5ldCIsImV4cCI6MTY1MTY1NzcwN30.Rj_XnWrEU0dJc2hTqxGqoAtGgIFX5HENn1XChNw3anvXjW2Xd7gISr602qhdTYDlg53WdhT-sYlpVxTSBLqpqw
                cookieInfo:  Max-Age=604800
                cookieInfo:  Expires=Tue, 10 May 2022 09:48:30 GMT
                cookieInfo:  Secure
                cookieInfo:  HttpOnly
                cookieInfo:  SameSite=None
                */
            }
            return cookieInfos[0].substring(13);
        }

        return null;

    }

    private String parseBearerToken(HttpServletRequest request) {
        // Http 리퀘스트의 헤더를 파싱해 Bearer 토큰을 리턴한다.
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}
