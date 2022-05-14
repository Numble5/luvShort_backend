package com.example.backend.controller;

import com.example.backend.domain.Chat.dto.ChatRoomInfoDto;
import com.example.backend.domain.Chat.dto.newChatRoomDto;
import com.example.backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    // 사용자 전체 채팅방 리스트
    @GetMapping("/chat/rooms/{userId}")
    public ResponseEntity<?> chatRooms(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(chatService.findAllRoom(userId), HttpStatus.OK);
    }

    // 채팅방 생성
    @PostMapping("/chat/room")
    public ResponseEntity<?> craeteRoom(@RequestBody newChatRoomDto newChatRoomDto) {
        return new ResponseEntity<>(chatService.createRoom(newChatRoomDto),HttpStatus.OK);
    }

    // 특정 채팅방 조회 -> 대화 목록 다 불러오기
    @GetMapping("/chat/room/{roomName}")
    public ResponseEntity<?> getAllChatAt(@PathVariable("roomName") String roomName) {
        return new ResponseEntity<>(chatService.getAllChatAtRoom(roomName),HttpStatus.OK);
    }
}
