package com.rey.review.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReviewExceptionHandler extends RuntimeException{

    private String errorCode;
    private String errorMessage;
    private HttpStatus httpStatus;


    public ReviewExceptionHandler( String errorCode, String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
