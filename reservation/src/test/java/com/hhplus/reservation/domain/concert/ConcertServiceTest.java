package com.hhplus.reservation.domain.concert;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.hhplus.reservation.application.dto.ConcertInfo;
import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import com.hhplus.reservation.application.dto.ConcertSeatInfo;
import com.hhplus.reservation.domain.concert.*;
import com.hhplus.reservation.support.error.BizException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    @Test
    @DisplayName("예약일정_조회성공")
    void 예약일정_조회성공() {
        Long concertId = 1L;
        List<ConcertSchedule> schedules = List.of(
                ConcertSchedule.builder().id(1L).concertId(concertId).build()
        );

        when(concertRepository.getSchedules(concertId)).thenReturn(schedules);

        List<ConcertScheduleInfo> result = concertService.getSchedules(concertId);

        assertEquals(1, result.size());
        verify(concertRepository).getSchedules(concertId);
    }

    @Test
    @DisplayName("예약일정_없음")
    void 예약일정_없음() {
        Long concertId = 99L;

        when(concertRepository.getSchedules(concertId)).thenReturn(List.of());

        assertThrows(BizException.class, () -> concertService.getSchedules(concertId));
    }

    @Test
    @DisplayName("좌석목록_조회성공")
    void 좌석목록_조회성공() {
        Long scheduleId = 1L;

        List<ConcertSeat> seats = List.of(
                ConcertSeat.builder()
                        .id(1L)
                        .concertScheduleId(scheduleId)
                        .seatNum("A1")
                        .seatPrice(50000L)
                        .status(ConcertSeatStatus.AVAILABLE)
                        .build()
        );

        when(concertRepository.getSeats(scheduleId)).thenReturn(seats);

        List<ConcertSeatInfo> result = concertService.getSeats(scheduleId);

        assertEquals(1, result.size());
        assertEquals(scheduleId, result.get(0).getConcertScheduleId());
        verify(concertRepository).getSeats(scheduleId);
    }

    @Test
    @DisplayName("좌석목록_없음")
    void 좌석목록_없음() {
        Long scheduleId = 99L;

        when(concertRepository.getSeats(scheduleId)).thenReturn(List.of());

        assertThrows(BizException.class, () -> concertService.getSeats(scheduleId));
    }

    @Test
    @DisplayName("콘서트정보_조회성공")
    void 콘서트정보_조회성공() {
        Long concertId = 1L;
        Concert concert = Concert.builder().id(concertId).title("Concert A").build();

        when(concertRepository.getConcert(concertId)).thenReturn(concert);

        ConcertInfo result = concertService.getConcert(concertId);

        assertEquals("Concert A", result.getTitle());
        verify(concertRepository).getConcert(concertId);
    }

    @Test
    @DisplayName("좌석예약_성공")
    void 좌석예약_성공() {
        Long scheduleId = 1L;
        List<Long> seats = List.of(1L, 2L);

        when(concertRepository.countSeatAvaliable(seats)).thenReturn(2L);
        when(concertRepository.getTotalPrice(seats)).thenReturn(100000L);

        Long totalPrice = concertService.updateSeatStatus(scheduleId, seats);

        assertEquals(100000L, totalPrice);
        verify(concertRepository).updateSeatsStatusWithLock(seats, ConcertSeatStatus.UNAVAILABLE);
        verify(concertRepository).updateAvailableSeats(scheduleId, seats.size());
    }

    @Test
    @DisplayName("좌석수_불일치")
    void 좌석수_불일치() {
        Long scheduleId = 1L;
        List<Long> seats = List.of(1L, 2L, 3L);

        when(concertRepository.countSeatAvaliable(seats)).thenReturn(2L);

        assertThrows(BizException.class, () -> concertService.updateSeatStatus(scheduleId, seats));
    }
}
