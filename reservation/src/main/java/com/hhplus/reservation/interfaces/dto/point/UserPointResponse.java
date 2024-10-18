package com.hhplus.reservation.interfaces.dto.point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPointResponse {
    long userId;
    long point;
}
