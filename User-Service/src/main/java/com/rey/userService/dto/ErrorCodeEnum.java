package com.rey.userService.dto;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {

    USER_NOT_FOUND("40000","USER NOT FOUND"),
    EMAIL_ALREADY_TAKEN("40002","EMAIL ALREADY TAKEN"),
    USERNAME_ALREADY_EXIST("40001","USERNAME ALREADY EXIST"),
    JOB_NOT_FOUND("40003","Jobs not Found"),
    JOB_SERVICE_UNAVAILABLE("40004","Job Service Unavailable"),
    REVIEW_SERVICE_UNAVAILABLE("40005","Review Service Unavailable"),
    COMPANY_ALREADY_EXIST("40006", "Company Already Exist"),
    GENERIC_ERROR("40007","Oops, Something went wrong..."),
    PASSWORD_MISMATCH("40008","PASSWORD MISMATCH"),
    INVALID_REQUEST("40009","INVALID REQUEST");

    private final String errorCode;
    private final String errorMessage;


    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
