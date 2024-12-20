package com.hhplus.reservation.support.error;

import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
    private final ErrorType errorType;

    public BizException(ErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
    }
}