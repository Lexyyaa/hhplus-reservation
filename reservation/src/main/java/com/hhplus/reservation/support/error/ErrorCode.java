package com.hhplus.reservation.support.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 공통 오류
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // 대기열 관련 오류
    TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰이 존재하지 않습니다."),
    VALIDATED_TOKEN(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다."),
    MAX_PROGRESS_EXCEEDED(HttpStatus.BAD_REQUEST, "최대 진행 수를 초과하였습니다."),
    EMPTY_QUEUE(HttpStatus.NOT_FOUND, "대기자가 존재하지 않습니다."),

    // 콘서트 관련 오류
    CONCERT_NOT_FOUND(HttpStatus.NOT_FOUND, "콘서트 정보가 존재하지 않습니다."),
    CONCERT_SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 가능한 날짜가 존재하지 않습니다."),
    SEATS_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 가능한 좌석이 존재하지 않습니다."),
    UNAVAILABLE_SEAT(HttpStatus.BAD_REQUEST, "예약할 수 없는 좌석이 포함되어 있습니다."),
    SEAT_ALREADY_RESERVED(HttpStatus.CONFLICT, "이미 예약된 좌석입니다."),

    // 예약 관련 오류
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 내역이 존재하지 않습니다."),
    RESERVATION_EXPIRED(HttpStatus.BAD_REQUEST, "예약이 만료되었습니다."),
    RESERVATION_LOCK_FAILED(HttpStatus.CONFLICT, "다른 사용자가 예약 중입니다."),
    INVALID_PAYMENT_AMOUNT(HttpStatus.BAD_REQUEST, "결제 금액이 올바르지 않습니다."),

    // 포인트 관련 오류
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
    INSUFFICIENT_POINTS(HttpStatus.BAD_REQUEST, "포인트가 부족합니다."),
    INVALID_CHARGE_AMOUNT(HttpStatus.BAD_REQUEST, "충전 포인트는 0보다 커야 합니다."),

    // 결제 관련 오류
    PAYMENT_ALREADY_MADE(HttpStatus.CONFLICT, "이미 결제된 예약입니다."),
    PAYMENT_EXPIRED(HttpStatus.BAD_REQUEST, "결제 시간이 초과되었습니다."),
    INVALID_PAYMENT_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 결제 토큰입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}