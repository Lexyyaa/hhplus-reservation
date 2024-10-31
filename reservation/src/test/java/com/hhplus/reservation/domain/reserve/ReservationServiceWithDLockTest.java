package com.hhplus.reservation.domain.reserve;

import com.hhplus.reservation.application.dto.ReservationInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceWithDLockTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private RLock rLock;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("락_획득성공_예약성공")
    void 락_획득성공_예약성공() throws InterruptedException {
        Long concertScheduleId = 1L;
        Long userId = 1L;
        Long totalPrice = 5000L;
        List<Long> seats = List.of(1L, 2L);

        // Mock 설정
        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(5, 10, TimeUnit.SECONDS)).thenReturn(true); // 락 획득 성공

        Reservation reservation = Reservation.create(userId, concertScheduleId, totalPrice, ReservationType.TEMP_RESERVED);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        List<ReservationSeat> mockSeats = List.of(
                new ReservationSeat(1L, reservation.getId(), 1L),
                new ReservationSeat(2L, reservation.getId(), 2L)
        );
        when(reservationRepository.saveAll(anyList())).thenReturn(mockSeats);

        ReservationInfo reservationInfo = reservationService.reserveWithDLock(concertScheduleId, userId, totalPrice, seats);

        assertEquals(concertScheduleId, reservationInfo.getConcertScheduleId());
        assertEquals(userId, reservationInfo.getUserId());
    }

    @Test
    @DisplayName("락_획득실패_예약실패")
    void 락_획득실패_예약실패() throws InterruptedException {
        Long concertScheduleId = 1L;
        Long userId = 1L;
        Long totalPrice = 5000L;
        List<Long> seats = List.of(101L, 102L);

        when(redissonClient.getLock(anyString())).thenReturn(rLock);
        when(rLock.tryLock(5, 10, TimeUnit.SECONDS)).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> reservationService.reserveWithDLock(concertScheduleId, userId, totalPrice, seats));
    }
}
