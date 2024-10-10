package com.hhplus.reservation.interfaces.dto.reserve;

import com.hhplus.reservation.domain.reserve.ReservationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReserveResponse {
    Long id;
    Long userId;
    Long concertDetailId;
    String concertTitle;
    LocalDate performDate;
    Long totalPrice;
    ReservationType isConfirmed;
    LocalDateTime reserveRequestAt;
    LocalDateTime reserveConfirmAt;
}
