package com.hhplus.reservation.interfaces.dto.payment;

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
public class PaymentResponse {
    Long id;
    Long userId;
    String concertTitle;
    LocalDate performDate;
    Long price;
    LocalDateTime paidAt;
}
