package com.example.backend.domain.Chat;

import com.example.backend.domain.BaseEntity;
import com.example.backend.domain.user.Interest;
import com.example.backend.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table
public class UserChat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_idx")
    private ChatRoom chatRoom;

    public UserChat(User user, ChatRoom chatRoom){
        this.user= user;
        this.chatRoom = chatRoom;
    }
}
