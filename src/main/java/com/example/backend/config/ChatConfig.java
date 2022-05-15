package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class ChatConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat").setAllowedOriginPatterns("*");
        //.withSocketJs();
        // socket 연결 시도 시 사용할 endpoint 등록
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        // queue 는 1:1 , topic 은 1:N에 많이 사용
        registry.setApplicationDestinationPrefixes("/pub"); // pub 시 기존 prefix

        // -> "queue" or "topic" 이 prefix로 붙을 경우 broker가 잡아서 해당 채팅방 구독하고 있는 클라이언트에 전달
        // 클라이언트가 경로의 시작에 "/app"로 메세지 전송 시 brocker로 보내짐
    }
}
