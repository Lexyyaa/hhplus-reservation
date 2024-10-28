package com.hhplus.reservation.domain.queue;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hhplus.reservation.domain.concert.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @Test
    @DisplayName("예약일정 없음")
    void 예약일정_없음() {
        Long concertId = 99L;
        when(concertRepository.getSchedules(concertId)).thenReturn(List.of());

        BizException exception = assertThrows(BizException.class,
                () -> concertService.getSchedules(concertId));

        assertEquals(ErrorType.CONCERT_SCHEDULE_NOT_FOUND, exception.getErrorType());
    }

    @Test
    @DisplayName("좌석목록 없음")
    void 좌석목록_없음() {
        Long scheduleId = 99L;
        when(concertRepository.getSeats(scheduleId)).thenReturn(List.of());

        BizException exception = assertThrows(BizException.class,
                () -> concertService.getSeats(scheduleId));

        assertEquals(ErrorType.SEATS_NOT_FOUND, exception.getErrorType());
    }

    @Test
    @DisplayName("콘서트 없음")
    void 콘서트_없음() {
        Long concertId = 99L;
        when(concertRepository.getConcert(concertId)).thenReturn(null);

        BizException exception = assertThrows(BizException.class,
                () -> concertService.getConcert(concertId));

        assertEquals(ErrorType.CONCERT_NOT_FOUND, exception.getErrorType());
    }

    @Test
    @DisplayName("좌석 예약 성공")
    void 좌석_예약_성공() {
        Long scheduleId = 1L;
        List<Long> seatIds = List.of(1L, 2L);
        when(concertRepository.countSeatAvaliable(seatIds)).thenReturn(2L);
        when(concertRepository.getTotalPrice(seatIds)).thenReturn(100000L);

        Long totalPrice = concertService.updateSeatStatus(scheduleId, seatIds);

        assertEquals(100000L, totalPrice);
        verify(concertRepository).updateSeatsStatusWithLock(seatIds, ConcertSeatStatus.UNAVAILABLE);
        verify(concertRepository).updateAvailableSeats(scheduleId, seatIds.size());
    }

    @Test
    @DisplayName("좌석 수 불일치")
    void 좌석_수_불일치() {
        Long scheduleId = 1L;
        List<Long> seatIds = List.of(1L, 2L, 3L);
        when(concertRepository.countSeatAvaliable(seatIds)).thenReturn(2L);

        BizException exception = assertThrows(BizException.class,
                () -> concertService.updateSeatStatus(scheduleId, seatIds));

        assertEquals(ErrorType.UNAVAILABLE_SEAT, exception.getErrorType());
    }
}
