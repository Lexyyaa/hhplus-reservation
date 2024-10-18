package com.hhplus.reservation.domain.queue;

import com.hhplus.reservation.domain.common.Timestamped;
import com.hhplus.reservation.interfaces.dto.queue.WaitingQueueResponse;
import com.hhplus.reservation.support.error.CustomException;
import com.hhplus.reservation.support.error.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.nio.file.Watchable;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "waiting_queue")
public class WaitingQueue extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private WaitingQueueStatus status;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    public static WaitingQueueResponse convert (WaitingQueue queue){
        return WaitingQueueResponse.builder()
                .userId(queue.getUserId())
                .token(queue.getToken())
                .status(queue.getStatus())
                .createdAt(queue.getCreatedAt()).build();
    }

    public static String makeToken(Long userId) {
        UUID uuid = UUID.nameUUIDFromBytes(userId.toString().getBytes());
        String tokenStr = userId + "__" + uuid;
        return Base64.getEncoder().encodeToString(tokenStr.getBytes());
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//    public static String makeToken(Long userId, Optional<WaitingQueue> optionalQueue){
//        if (optionalQueue.isPresent()) {
//            return optionalQueue.get().getToken();
//        }else{
//            return makeToken(userId);
//        }
//    }

    public static void validateToken(boolean isValidToken){
        if(!isValidToken){
            throw new CustomException(ErrorCode.VALIDATED_TOKEN);
        }
    }

    public static void checkToken(boolean isPresentToken){
        System.out.println("checkToken isPresentToken  :" + isPresentToken);
        if(!isPresentToken){
             throw new CustomException(ErrorCode.TOKEN_NOT_FOUND);
        }
    }

    public static void isValidCount(Long currProgressCnt) {
        if (currProgressCnt >= 5) {
            throw new CustomException(ErrorCode.MAX_PROGRESS_EXCEEDED);
        }
    }

    public static void isValidTokenList(List<WaitingQueue> waitingQueues) {
        if (waitingQueues == null || waitingQueues.isEmpty()) {
             throw new CustomException(ErrorCode.EMPTY_QUEUE);
        }
    }
}













