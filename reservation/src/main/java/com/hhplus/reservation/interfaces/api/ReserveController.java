package com.hhplus.reservation.interfaces.api;

import com.hhplus.reservation.domain.concert.ConcertSeatType;
import com.hhplus.reservation.domain.reserve.ReservationType;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveResponse;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveSeatRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@RestController
@RequestMapping("/api/reserve")
public class ReserveController {

    // Mock 대기열 (유효한 토큰 저장)
    public static Queue<String> waitingQueue = new LinkedList<>();

    static {
        // 미리 대기열에 토큰 추가
        waitingQueue.add("MTA6ZGM0ZDU0NDYtNDg5NC00NzgzLWFmMmYtODZiYjUwNDgxOTdh");
    }

    // 좌석예약 API
    @PostMapping("{concertDetailId}/seat")
    public ResponseEntity<ReserveResponse> reserveSeat(
            @RequestHeader("Authorization") String queueToken,
            @PathVariable("concertDetailId") Long concertDetailId,
            @RequestBody ReserveSeatRequest reserveSeatRequest
    ) {

        validateToken(queueToken);

        ReserveSeatRequest mockRequest = new ReserveSeatRequest();
        List<ReserveSeatRequest.ReserveSeat> seatList = List.of(
                new ReserveSeatRequest.ReserveSeat(concertDetailId,"S1", ConcertSeatType.S,150000L),
                new ReserveSeatRequest.ReserveSeat(concertDetailId,"R1", ConcertSeatType.R,100000L),
                new ReserveSeatRequest.ReserveSeat(concertDetailId,"A1", ConcertSeatType.A,50000L),
                new ReserveSeatRequest.ReserveSeat(concertDetailId,"A2", ConcertSeatType.A,50000L)
        );
        mockRequest.setSeatList(seatList);
        mockRequest.setUserId(10L);

        ReserveResponse reserveResponse = getMockReserveInfo(mockRequest);

        return ResponseEntity.ok().body(reserveResponse);
    }

    // Stub
    private ReserveResponse getMockReserveInfo(ReserveSeatRequest mockRequest){

        ReserveResponse mockResponcse = new ReserveResponse(
                5L,
                10L,
                10L,
                "흠뻑쇼",
                LocalDate.parse("2025-08-08"),
                250000L,
                ReservationType.NOT_CONFIRMED,
                LocalDateTime.now(),
                null);
        return mockResponcse;
    }

    private void validateToken(String queueToken) {
        if (queueToken == null || !waitingQueue.contains(queueToken)) {
            throw new IllegalArgumentException("유효하지 않은 접근입니다.");
        }
    }
}



