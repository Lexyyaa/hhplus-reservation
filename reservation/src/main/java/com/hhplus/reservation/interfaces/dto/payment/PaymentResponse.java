package com.hhplus.reservation.interfaces.dto.payment;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    Long id;
    Long userId;
    String concertTitle;
    LocalDate performDate;
    Long price;
    LocalDateTime paidAt;
}
