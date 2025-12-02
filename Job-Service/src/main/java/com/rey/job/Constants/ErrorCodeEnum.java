package com.rey.job.Constants;

import lombok.Getter;
import org.hibernate.JDBCException;

@Getter
public enum ErrorCodeEnum {

    INVALID_REQUEST("10000","Invalid Request"),
    JOB_NOT_FOUND("10001","Job Not Found"),
    GENERIC_ERROR("10002","Ooopppss something went wrong"),
    REVIEW_SERVICE_UNAVAILABLE("10003","Review Service Unavailable"),
    COMPANY_SERVICE_UNAVAILABLE("10004","Company Service Unavailable"),
    INVALID_SALARY("10005","Invalid Salary"),
    INVALID_DESCRIPTION("10006","Invalid Description"),
    INVALID_LOCATION("10007","Invalid Location");
    private final String errorCode;
    private final String errorMessage;

    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
