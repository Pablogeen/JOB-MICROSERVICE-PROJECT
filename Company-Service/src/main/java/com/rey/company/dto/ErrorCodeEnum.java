package com.rey.company.dto;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {



    COMPANY_NOT_FOUND("30000","Company not Found"),
    REVIEW_NOT_FOUND("30003","Reviews not Found"),
    INVALID_COMPANY_NAME("30001","Invalid Company Name"),
    INVALID_COMPANY_DESCRIPTION("30002","Invalid Company Description"),
    JOB_NOT_FOUND("30003","Jobs not Found"),
    JOB_SERVICE_UNAVAILABLE("30004","Job Service Unavailable"),
    REVIEW_SERVICE_UNAVAILABLE("30005","Review Service Unavailable");

    private String errorCode;
    private String errorMessage;
    private static String validationMessage;

    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
