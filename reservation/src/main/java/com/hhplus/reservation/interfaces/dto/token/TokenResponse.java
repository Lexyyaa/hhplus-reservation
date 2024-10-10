package com.hhplus.reservation.interfaces.dto.token;

import com.hhplus.reservation.domain.token.TokenStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    private long userId;

    private String token;

    private long waitNum;

    private TokenStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;
}
