package com.hhplus.reservation.support.error;

import lombok.Builder;
import org.springframework.http.ResponseEntity;

@Builder
public record ErrorResponse(
        int status,
        String code,
        String message
) {
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorType errorType) {
        return ResponseEntity
                .status(errorType.getErrorCode().getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(errorType.getErrorCode().getHttpStatus().value())
                        .code(errorType.name())
                        .message(errorType.getMessage())
                        .build());
    }
}