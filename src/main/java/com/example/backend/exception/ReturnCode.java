package com.example.backend.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReturnCode {

    USER_EXIST_USING_THIS_EMAIL(400, "해당 이메일로 이미 가입한 계정이 존재합니다.");
    private final int status;
    private final String message;
}
