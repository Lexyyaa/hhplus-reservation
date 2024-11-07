package com.hhplus.reservation.domain.queue;

import com.hhplus.reservation.interfaces.dto.queue.WaitingQueueResponse;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WaitingQueueRedis {

    private String token;

    private LocalDateTime expiredAt;

    public static WaitingQueueResponse convert (WaitingQueueRedis queue){
        return WaitingQueueResponse.builder()
                .token(queue.getToken()).build();
    }

    public static String makeToken(Long userId) {
        UUID uuid = UUID.nameUUIDFromBytes(userId.toString().getBytes());
        String tokenStr = userId + "__" + uuid;
        return Base64.getEncoder().encodeToString(tokenStr.getBytes());
    }

    public static void isValidCount(long currProgressCnt) {
        if (currProgressCnt >= 5) {
            throw new BizException(ErrorType.MAX_PROGRESS_EXCEEDED);
        }
    }
}

