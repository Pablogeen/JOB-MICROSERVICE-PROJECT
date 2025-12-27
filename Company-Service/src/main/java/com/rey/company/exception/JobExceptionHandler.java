package com.rey.company.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class JobExceptionHandler extends RuntimeException{

    private String errorCode;
    private String errorMessage;
    private HttpStatus status;

    public JobExceptionHandler(String errorCode, String errorMessage, HttpStatus status) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }
}
