package com.hhplus.reservation.interfaces.dto.queue;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingQueuePollingResponse {
    private Long waitNum;
    private LocalDateTime createdAt;
}