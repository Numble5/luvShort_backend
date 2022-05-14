package com.example.backend.domain.Chat.dto;

import com.example.backend.domain.Chat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
public class ChatMessageDto {
    String sender;
    String content;
    String roomName;
    LocalDateTime createdDate;

    public ChatMessageDto(ChatMessage chatMessage) {
        this.sender = chatMessage.getUser().getEmail();
        this.content = chatMessage.getMessage();
        this.createdDate = chatMessage.getCreatedDate();
    }

}
