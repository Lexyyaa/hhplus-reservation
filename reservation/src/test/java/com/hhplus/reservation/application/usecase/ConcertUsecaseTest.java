package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.interfaces.dto.concert.ConcertScheduleResponse;
import com.hhplus.reservation.interfaces.dto.concert.ConcertSeatResponse;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class ConcertUsecaseTest {

    @Autowired
    private ConcertUsecase concertUsecase;

    @Test
    @DisplayName("콘서트 일정 조회 성공")
    void 콘서트_일정_조회_성공() {
        // given
        Long concertId = 1L;

        // when
        List<ConcertScheduleResponse> schedules = concertUsecase.getSchedules(concertId);

        // then
        assertThat(schedules).isNotEmpty();
        assertThat(schedules.size()).isEqualTo(2);  // 해당 콘서트에 일정 2개 존재
        assertThat(schedules.get(0).getConcertId()).isEqualTo(concertId);
    }

    @Test
    @DisplayName("콘서트 좌석 조회 성공")
    void 콘서트_좌석_조회_성공() {
        // given
        Long scheduleId = 1L;

        // when
        List<ConcertSeatResponse> seats = concertUsecase.getSeats(scheduleId);

        // then
        assertThat(seats).isNotEmpty();
        assertThat(seats.size()).isEqualTo(6);  // 해당 일정에 좌석 6개 존재
        assertThat(seats.get(0).getConcertScheduleId()).isEqualTo(scheduleId);
    }

    @Test
    @DisplayName("빈 좌석 목록 조회")
    void 빈_좌석_목록_조회_예외() {

        Long scheduleId = 4L;

        // when & then
        BizException exception = assertThrows(BizException.class,
                () -> concertUsecase.getSeats(scheduleId));

        assertThat(exception.getErrorType()).isEqualTo(ErrorType.SEATS_NOT_FOUND);
    }

    @Test
    @DisplayName("존재하지 않는 콘서트 일정 조회")
    void 존재하지_않는_콘서트_일정_조회() {
        Long invalidConcertId = 99L;

        BizException exception = assertThrows(BizException.class,
                () -> concertUsecase.getSchedules(invalidConcertId));

        assertThat(exception.getErrorType()).isEqualTo(ErrorType.CONCERT_SCHEDULE_NOT_FOUND);
    }
}
