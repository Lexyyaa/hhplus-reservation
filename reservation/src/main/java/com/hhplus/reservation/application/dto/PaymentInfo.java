package com.hhplus.reservation.application.dto;

import com.hhplus.reservation.interfaces.dto.payment.PaymentResponse;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentInfo {
    Long id;
    Long userId;
    String concertTitle;
    LocalDate performDate;
    Long price;
    LocalDateTime paidAt;

    public static PaymentResponse convert(Long id,Long userId, String title, Long price){
        return PaymentResponse.builder()
                .id(id)
                .userId(userId)
                .concertTitle(title)
                .performDate(LocalDate.now())
                .price(price)
                .build();
    }
}
