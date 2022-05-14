package com.example.backend.repository;

import com.example.backend.domain.Chat.ChatMessage;
import com.example.backend.domain.Chat.ChatRoom;
import com.example.backend.domain.Chat.UserChat;
import com.example.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {

    Optional<ChatRoom> findByRoomName(String roomName);

    @Query("select cr.messages from ChatRoom cr where cr.roomName = :roomName")
    List<ChatMessage> findAllChatMessageByRoomName(String roomName);
}
