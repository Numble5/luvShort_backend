package com.example.backend.domain.Chat;

import com.example.backend.domain.BaseEntity;
import com.example.backend.domain.Chat.enums.MessageType;
import com.example.backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table
public class ChatMessage extends BaseEntity {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    //채팅방
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_idx")
    private ChatRoom chatRoom;
    // 보내는 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;
    // 내용
    private String message;

    @Builder
    public ChatMessage(ChatRoom chatRoom, User user, String message) {
        this.chatRoom = chatRoom;
        this.user = user;
        this.message = message;
    }
}
