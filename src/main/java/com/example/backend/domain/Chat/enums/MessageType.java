package com.example.backend.domain.Chat.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageType {

        ENTER("ENTER"),
        TALK("TALK");

    private final String message;

}
