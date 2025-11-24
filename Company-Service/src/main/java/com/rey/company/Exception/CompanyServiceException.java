package com.rey.company.Exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CompanyServiceException extends RuntimeException{

    private String errorCode;
    private String errorMessage;
    private HttpStatus httpStatus;

    public CompanyServiceException( String errorCode, String errorMessage, HttpStatus httpStatus) {
        super(errorMessage);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
