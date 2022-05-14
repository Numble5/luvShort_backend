package com.example.backend.controller;

import com.example.backend.domain.Chat.ChatMessage;
import com.example.backend.domain.Chat.dto.ChatMessageDto;
import com.example.backend.domain.Chat.enums.MessageType;
import com.example.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessageSendingOperations sendingOperations;
    private final ChatService chatService;
    @MessageMapping("/chat/message") // 실제로는 pub/chat/message
    public void enter(ChatMessageDto message) {


            // 메세지 저장
        chatService.saveMessage(message);

        sendingOperations.convertAndSend("/sub/chat/room/" + message.getRoomName());
    }
}
