package com.rey.job.exception;


import com.rey.job.constants.ErrorCodeEnum;
import com.rey.job.pojo.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JobExceptionHandler.class)
    public ResponseEntity<ErrorResponse> handleReviewExceptionHandler(JobExceptionHandler e){
        log.info("Handling ReviewExceptionHandler: {}",e.getErrorMessage());
        ErrorResponse errorResponse = new ErrorResponse(e.getErrorCode(),e.getErrorMessage());
        return new ResponseEntity<>(errorResponse, e.getStatus());

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handlePaypalException(Exception e){
        log.info("Handling Generic Exception: {}",e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                ErrorCodeEnum.GENERIC_ERROR.getErrorCode(),
                ErrorCodeEnum.GENERIC_ERROR.getErrorMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.error("Handing Invalid Request");
        String message = Optional.ofNullable(ex.getBindingResult().getFieldError())
                .map(FieldError::getDefaultMessage)
                .orElse("Validation error");


        return ResponseEntity.badRequest().body(new ErrorResponse(
                        ErrorCodeEnum.INVALID_REQUEST.getErrorCode(),
                        message
                ));
    }
}
