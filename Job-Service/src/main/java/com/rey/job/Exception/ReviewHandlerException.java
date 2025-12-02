package com.rey.job.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReviewHandlerException extends RuntimeException {


    private String errorCode;
    private String errorMessage;
    private HttpStatus status;

    public ReviewHandlerException(String errorCode, String errorMessage, HttpStatus status) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }
}
