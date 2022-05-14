package com.example.backend.repository;

import com.example.backend.domain.Chat.ChatRoom;
import com.example.backend.domain.Chat.UserChat;
import com.example.backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChatRepository extends JpaRepository<UserChat,Long> {


    @Query("select uc.chatRoom from UserChat uc where uc.user = :user")
    List<ChatRoom> findAllChatRoomByUser(User user);

    @Query("select uc.chatRoom from UserChat uc where uc.user = :user and uc.chatRoom = :chatRoom")
    Optional<UserChat> findByChatRoomAndUser(ChatRoom chatRoom, User user);

    @Query("select uc.chatRoom from UserChat uc where uc.user.email = :email")
    List<ChatRoom> findAllByUser(String email);

}
