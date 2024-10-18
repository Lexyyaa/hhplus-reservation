package com.hhplus.reservation.interfaces.dto.queue;

import com.hhplus.reservation.domain.queue.WaitingQueueStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingQueueResponse {
    private long userId;
    private String token;
    private WaitingQueueStatus status;
    private LocalDateTime createdAt;
}
















