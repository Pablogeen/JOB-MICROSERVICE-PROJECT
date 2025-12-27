package com.rey.company.dto;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {

    COMPANY_NOT_FOUND("30000","Company not Found"),
    REVIEW_NOT_FOUND("30002","Reviews not Found"),
    INVALID_REQUEST("30001","Invalid"),
    JOB_NOT_FOUND("30003","Jobs not Found"),
    JOB_SERVICE_UNAVAILABLE("30004","Job Service Unavailable"),
    REVIEW_SERVICE_UNAVAILABLE("30005","Review Service Unavailable"),
    COMPANY_ALREADY_EXIST("30006", "Company Already Exist"),
    GENERIC_ERROR("30007","Oops, Something went wrong...");

    private String errorCode;
    private String errorMessage;


    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
