package com.hhplus.reservation.interfaces.dto.reserve;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveDetailResponse {
    Long id;
    Long reservationId;
    Long concertSeatId;
}
