package com.hhplus.reservation.application.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertInfo {
    private Long id;
    private String title;
}
