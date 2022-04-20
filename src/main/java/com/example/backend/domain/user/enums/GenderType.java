package com.example.backend.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter @RequiredArgsConstructor
public enum GenderType {

    MALE("남성"),
    FEMALE("여성"),
    NONE("응답없음");

    private final String gender;
}
