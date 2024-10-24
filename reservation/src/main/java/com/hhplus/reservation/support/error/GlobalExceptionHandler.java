package com.hhplus.reservation.support.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(BizException ex) {
//        // error log handling
//        switch (e.getErrorType().getLogLevel()) {
//            case Error -> log.error();
//            case Warn -> log.warn();
//            default -> log.info();
//        }
//        // HTTP STATUS CODE MAPPING
//        switch (e.getErrorType().getErrorCode()) {
//            --->
//            case DB_ERROR, KAKA_ERROR -> 500;
//            case CLIENT_ERROR -> 400;
//            case NOT_FOUND -> 404;
//            default -> HttpStatus .200; ( 200 Error 표현하는 케이스 )
//        }}
        return ErrorResponse.toResponseEntity(ex.getErrorType());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return ErrorResponse.toResponseEntity(ErrorType.INTERNAL_SERVER_ERROR);
    }
}

//https://github.com/hyunn12/hhplus-server-reservation/pull/21