package com.example.backend.exception;

import lombok.Getter;

@Getter
public class BackendException extends RuntimeException{
    ReturnCode returnCode;

    public BackendException(ReturnCode returnCode){
        this.returnCode = returnCode;
    }
}
