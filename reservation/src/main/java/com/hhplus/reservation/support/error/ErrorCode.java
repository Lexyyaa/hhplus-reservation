package com.hhplus.reservation.support.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    CLIENT_ERROR(HttpStatus.BAD_REQUEST),
    CONFLICT_ERROR(HttpStatus.CONFLICT),
    UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED);

    private final HttpStatus httpStatus;
}