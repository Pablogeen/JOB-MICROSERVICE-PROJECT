package com.rey.review.constant;

import lombok.Getter;

@Getter
public enum ErrorCodeEnum {

    INVALID_REQUEST("2000","Invalid Request"),
    INVALID_RATING("20001","Invalid Rating"),
    GENERIC_ERROR("20002","Ooopppss something went wrong"),
    REVIEW_NOT_FOUND("20003","Review not found");

    private final String errorCode;
    private final String errorMessage;

    ErrorCodeEnum(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
