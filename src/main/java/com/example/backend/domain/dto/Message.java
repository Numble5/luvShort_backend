package com.example.backend.domain.dto;

import com.example.backend.exception.ReturnCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Message {
    private Integer status;
    private String message;
    private Object data;

    @Builder
    public Message(ReturnCode code,Object data) {
        this.status = code.getStatus();
        this.message = code.getMessage();
        this.data = data;
    }
}
