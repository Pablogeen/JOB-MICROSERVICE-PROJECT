package com.rey.job.Exception;

import org.springframework.http.HttpStatus;

public class CompanyHandlerException extends RuntimeException {

    private String errorCode;
    private String errorMessage;
    private HttpStatus status;

    public CompanyHandlerException(String errorCode, String errorMessage, HttpStatus status) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.status = status;
    }
}
