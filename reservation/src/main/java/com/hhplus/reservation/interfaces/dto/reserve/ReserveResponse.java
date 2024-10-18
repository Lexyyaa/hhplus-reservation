package com.hhplus.reservation.interfaces.dto.reserve;

import com.hhplus.reservation.domain.reserve.ReservationType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveResponse {
    Long id;
    Long userId;
    Long concertScheduleId;
    Long totalPrice;
    ReservationType reserved;
    LocalDateTime reserveRequestAt;
    LocalDateTime reserveExpiredAt;
}
