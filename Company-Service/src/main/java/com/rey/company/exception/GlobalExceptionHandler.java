package com.rey.company.exception;

import com.rey.company.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CompanyServiceException.class)
    public ResponseEntity<ErrorResponse> handleCompanyServiceException(CompanyServiceException ex){
        log.info("About to throw custom exception");
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode(), ex.getErrorMessage());
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());

    }
}
