package com.example.backend.service;

import com.example.backend.domain.Chat.ChatMessage;
import com.example.backend.domain.Chat.ChatRoom;
import com.example.backend.domain.Chat.UserChat;
import com.example.backend.domain.Chat.dto.ChatMessageDto;
import com.example.backend.domain.Chat.dto.ChatRoomInfoDto;
import com.example.backend.domain.Chat.dto.newChatRoomDto;
import com.example.backend.domain.Chat.enums.MessageType;
import com.example.backend.domain.user.User;
import com.example.backend.repository.ChatMessageRepository;
import com.example.backend.repository.ChatRoomRepository;
import com.example.backend.repository.UserChatRepository;
import com.example.backend.repository.UserRepository;
import jdk.nashorn.internal.ir.Optimistic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final UserChatRepository userChatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    // 채팅방 불러오기
    @Transactional
    public List<ChatRoomInfoDto> findAllRoom(Long userIdx) {
        // 사용자 찾기
        User user  = userRepository.findById(userIdx)
                .orElseThrow(() -> new IllegalArgumentException("존재하지않는사용자"));

        // 사용자가 속한 chatRoom 모두 찾기
        List<ChatRoom> chatRooms= userChatRepository.findAllChatRoomByUser(user);

        // dto 생성
        List<ChatRoomInfoDto> chatRoomInfoDtos = new ArrayList<>();
        for(ChatRoom c: chatRooms) {
            chatRoomInfoDtos.add(new ChatRoomInfoDto(c));
        }
        return chatRoomInfoDtos;
    }

    // 채팅방 정보 하나 불러오기
    public ChatRoomInfoDto findRoomById(String roomName) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomName(roomName)
                .orElseThrow(()-> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return new ChatRoomInfoDto(chatRoom);
    }

    // 채팅방 생성 + 관계 생성
    @Transactional
    public  ChatRoomInfoDto createRoom(newChatRoomDto request) {
        // user1,user2 사이 이미 채팅방 존재하는지 확인
        List<ChatRoom> userChat1 = userChatRepository.findAllByUser(request.getUser1());
        List<ChatRoom> userChat2 = userChatRepository.findAllByUser(request.getUser2());
        
        ChatRoom chatRoom = null;
        // 같이 속한 방 찾기
        for(ChatRoom c: userChat1) {
            if(userChat2.contains(c)) chatRoom = c;
        }
        if(chatRoom == null) {
            chatRoom = ChatRoom.create();
            chatRoomRepository.save(chatRoom);

            // 사용자와 관계 저장
            createRelation(chatRoom.getRoomName(), request.getUser1());
            createRelation(chatRoom.getRoomName(), request.getUser2());
            
        }
        return new ChatRoomInfoDto(chatRoom);

    }

    // user-사용자 관계 생성하기
    public UserChat createRelation(String roomName,String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("사용자를 찾응 수 없습니다."));
        ChatRoom chatRoom = chatRoomRepository.findByRoomName(roomName)
                .orElseThrow(()-> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        // 이미 존재하는 관계인지 확인(sub or pub 중?)
        Optional<UserChat> already = userChatRepository.findByChatRoomAndUser(chatRoom,user);
        if(already.isPresent()) return already.get(); // 이미 관계 맺었으므로
        UserChat userChat = new UserChat(user,chatRoom);
        userChatRepository.save(userChat);
        return null; // 존재하던게 없었다

    }

    @Transactional
    public List<ChatMessageDto> getAllChatAtRoom(String roomName) {
        ChatRoom chatRoom = chatRoomRepository.findByRoomName(roomName)
                .orElseThrow(()-> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        List<ChatMessage> chatMessages = chatRoomRepository.findAllChatMessageByRoomName(roomName);
        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();
        for(ChatMessage c: chatMessages) {
            chatMessageDtos.add(new ChatMessageDto(c));
        }
        return chatMessageDtos;
    }

    // 메세지 저장하기
    public ChatMessageDto saveMessage(ChatMessageDto request) {
        User user = userRepository.findByEmail(request.getSender()).orElseThrow(() -> new IllegalArgumentException("사용자를 찾응 수 없습니다."));
        ChatRoom chatRoom = chatRoomRepository.findByRoomName(request.getRoomName())
                .orElseThrow(()-> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        ChatMessage chatMessage = ChatMessage.builder()
                .message(request.getContent())
                .chatRoom(chatRoom)
                .user(user)
                .build();


        chatMessageRepository.save(chatMessage);

        return new ChatMessageDto(chatMessage);
    }
}
