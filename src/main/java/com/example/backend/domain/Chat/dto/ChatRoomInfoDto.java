package com.example.backend.domain.Chat.dto;


import com.example.backend.domain.Chat.ChatRoom;
import com.example.backend.domain.Chat.UserChat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChatRoomInfoDto {
    private Long roomIdx;
    private String roomName;
    private List<String> users; // 참여자 이메일
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;

    public ChatRoomInfoDto(ChatRoom chatRoom) {
        this.roomIdx = chatRoom.getIdx();
        this.roomName = chatRoom.getRoomName();
        this.createdTime = chatRoom.getCreatedDate();
        this.updatedTime = chatRoom.getUpdatedDate();

        List<String> users = new LinkedList<>();
        for(UserChat uc: chatRoom.getUserChats()) {
            users.add(uc.getUser().getEmail());
        }
        this.users = users;
    }
}
