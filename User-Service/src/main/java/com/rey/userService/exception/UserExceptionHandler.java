package com.rey.userService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserExceptionHandler extends RuntimeException{

    private String errorCode;
    private String errorMessage;
    private HttpStatus status;

    public UserExceptionHandler(String errorCode, String errorMessage, HttpStatus status) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }
}
