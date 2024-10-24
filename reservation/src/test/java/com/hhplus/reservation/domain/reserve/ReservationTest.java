package com.hhplus.reservation.domain.reserve;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {
    @Test
    @DisplayName("예약을 생성한다")
    void 예약을_생성한다() {
        Reservation reservation = Reservation.create(1L, 2L, 50000L, ReservationType.RESERVED);
        assertEquals(1L, reservation.getUserId());
    }

    @Test
    @DisplayName("예약 좌석 리스트를 생성한다")
    void 예약_좌석_리스트를_생성한다() {
        List<ReservationSeat> seats = ReservationSeat.createList(1L, List.of(1L, 2L));
        assertEquals(2, seats.size());
    }
}