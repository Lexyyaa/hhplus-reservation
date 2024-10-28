package com.hhplus.reservation.support.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    // 공통 오류
    BAD_REQUEST(ErrorCode.CLIENT_ERROR, "잘못된 요청입니다.", LogLevel.ERROR),
    INTERNAL_SERVER_ERROR(ErrorCode.SERVER_ERROR, "서버 내부 오류가 발생했습니다.", LogLevel.ERROR),

    // 대기열 관련 오류
    TOKEN_NOT_FOUND(ErrorCode.NOT_FOUND, "대기중인 토큰이 존재하지 않습니다.", LogLevel.WARN),
    VALIDATED_TOKEN(ErrorCode.CLIENT_ERROR, "유효하지 않은 토큰입니다.", LogLevel.WARN),
    MAX_PROGRESS_EXCEEDED(ErrorCode.SERVER_ERROR, "진입 가능 수를 초과하였습니다.", LogLevel.WARN),
    EMPTY_QUEUE(ErrorCode.NOT_FOUND, "대기자가 존재하지 않습니다 ", LogLevel.WARN),

    // 콘서트 관련 오류
    CONCERT_NOT_FOUND(ErrorCode.NOT_FOUND, "콘서트 정보가 존재하지 않습니다.", LogLevel.WARN),
    CONCERT_SCHEDULE_NOT_FOUND(ErrorCode.NOT_FOUND, "예약 가능한 날짜가 존재하지 않습니다.", LogLevel.WARN),
    SEATS_NOT_FOUND(ErrorCode.NOT_FOUND, "예약 가능한 좌석이 존재하지 않습니다.", LogLevel.WARN),
    UNAVAILABLE_SEAT(ErrorCode.CLIENT_ERROR, "예약할 수 없는 좌석이 포함되어 있습니다.", LogLevel.WARN),

    // 예약 관련 오류
    RESERVATION_NOT_FOUND(ErrorCode.NOT_FOUND, "예약 내역이 존재하지 않습니다.", LogLevel.WARN),
    RESERVATION_EXPIRED(ErrorCode.SERVER_ERROR, "예약이 만료되었습니다.", LogLevel.WARN),
    SEAT_ALREADY_RESERVED(ErrorCode.CONFLICT_ERROR, "이미 예약된 좌석입니다.", LogLevel.WARN),

    // 포인트 관련 오류
    USER_NOT_FOUND(ErrorCode.NOT_FOUND, "존재하지 않는 사용자입니다.", LogLevel.WARN),
    INSUFFICIENT_POINTS(ErrorCode.CLIENT_ERROR, "포인트가 부족합니다.", LogLevel.WARN),
    INVALID_CHARGE_AMOUNT(ErrorCode.CLIENT_ERROR, "충전 포인트는 0보다 커야 합니다.", LogLevel.WARN),

    // 결제 관련 오류
    INVALID_PAYMENT_AMOUNT(ErrorCode.VALIDATION_ERROR, "결제 금액이 올바르지 않습니다.", LogLevel.WARN),
    PAYMENT_ALREADY_MADE(ErrorCode.CONFLICT_ERROR, "이미 결제된 예약입니다.", LogLevel.WARN),
    PAYMENT_EXPIRED(ErrorCode.SERVER_ERROR, "결제 시간이 초과되었습니다.", LogLevel.WARN),
    INVALID_PAYMENT_TOKEN(ErrorCode.UNAUTHORIZED_ERROR, "유효하지 않은 결제 토큰입니다.", LogLevel.WARN);

    private final ErrorCode errorCode;
    private final String message;
    private final LogLevel logLevel;
}