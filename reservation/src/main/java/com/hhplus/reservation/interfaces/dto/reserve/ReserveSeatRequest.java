package com.hhplus.reservation.interfaces.dto.reserve;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveSeatRequest {
    private Long userId;
    private List<Long> seats;
}
