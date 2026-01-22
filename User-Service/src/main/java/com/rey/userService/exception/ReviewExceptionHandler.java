package com.rey.userService.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReviewExceptionHandler extends RuntimeException {


    private final String errorCode;
    private final String errorMessage;
    private final HttpStatus status;

    public ReviewExceptionHandler(String errorCode, String errorMessage, HttpStatus status) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }
}
