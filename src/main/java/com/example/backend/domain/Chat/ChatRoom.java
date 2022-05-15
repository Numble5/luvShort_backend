package com.example.backend.domain.Chat;

import com.example.backend.domain.BaseEntity;
import com.example.backend.domain.video.Video;
import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@Table
public class ChatRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="room_idx")
    private Long idx;

    private String roomName;

    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private List<ChatMessage> messages = new LinkedList<>();

    // 채팅방과 관련된 user
    @OneToMany(mappedBy = "chatRoom",fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private List<UserChat> userChats = new LinkedList<>();

    public static ChatRoom create() {
        ChatRoom room = new ChatRoom();
        room.roomName = UUID.randomUUID().toString();
        return room;
    }
}
