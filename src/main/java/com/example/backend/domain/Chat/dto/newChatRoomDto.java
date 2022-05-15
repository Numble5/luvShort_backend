package com.example.backend.domain.Chat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class newChatRoomDto {

    // 1:1 채팅방 user들의 email
    String user1;
    String user2;


}
