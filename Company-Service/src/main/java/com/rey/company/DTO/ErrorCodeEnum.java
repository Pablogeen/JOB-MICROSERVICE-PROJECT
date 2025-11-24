package com.rey.company.DTO;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {

    COMPANY_NOT_FOUND("30000","Company not Found"),
    INVALID_COMPANY_NAME("30001","Invalid Company Name"),
    INVALID_COMPANY_DESCRIPTION("30002","Invalid Company Description");

    private String errorCode;
    private String errorMessage;

    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
