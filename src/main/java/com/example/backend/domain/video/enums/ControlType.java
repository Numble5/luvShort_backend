package com.example.backend.domain.video.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ControlType {
    AVAIL("이용가능"),
    CENSORED("관리자제한");

    private final String gender;
}
