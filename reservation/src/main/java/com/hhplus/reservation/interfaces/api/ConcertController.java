package com.hhplus.reservation.interfaces.api;

import com.hhplus.reservation.domain.concert.ConcertSeatStatus;
import com.hhplus.reservation.domain.concert.ConcertSeatType;
import com.hhplus.reservation.interfaces.dto.concert.ConcertDetailResponse;
import com.hhplus.reservation.interfaces.dto.concert.ConcertSeatResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@RestController
@RequestMapping("/api/concert")
public class ConcertController {

    // Mock 대기열 (유효한 토큰 저장)
    public static Queue<String> waitingQueue = new LinkedList<>();

    static {
        // 미리 대기열에 토큰 추가
        waitingQueue.add("MTA6ZGM0ZDU0NDYtNDg5NC00NzgzLWFmMmYtODZiYjUwNDgxOTdh");
    }

    // 예약 가능 날짜 API
    @GetMapping("/avaliable/{concertId}")
    public ResponseEntity<List<ConcertDetailResponse>> getAvailableDate(
            @RequestHeader("Authorization") String queueToken,
            @PathVariable Long concertId) {

        validateToken(queueToken);

        List<ConcertDetailResponse> avaliableDateList = getAvailableDateList(10L);
        return ResponseEntity.ok().body(avaliableDateList);
    }

    // Mock API용 메소드
    private List<ConcertDetailResponse> getAvailableDateList(Long concertId){
        List<ConcertDetailResponse> avaliableDateList = List.of(
            new ConcertDetailResponse(1L,10L, LocalDate.parse("2024-10-10"),10L,5L, ConcertSeatStatus.AVAILABLE),
            new ConcertDetailResponse(2L,10L, LocalDate.parse("2024-10-11"),10L,4L, ConcertSeatStatus.AVAILABLE),
            new ConcertDetailResponse(3L,10L, LocalDate.parse("2024-10-12"),10L,3L, ConcertSeatStatus.AVAILABLE)
        );
        return avaliableDateList;
    }

    // 예약 가능 좌석 API
    @GetMapping("/avaliable/seat/{concertDetailId}")
    public ResponseEntity<List<ConcertSeatResponse>> getAvailableSeat(
            @RequestHeader("Authorization") String queueToken, @PathVariable long concertDetailId) {

        validateToken(queueToken);

        List<ConcertSeatResponse> avaliableSeatList = getAvailableSeatList(concertDetailId);
        return ResponseEntity.ok().body(avaliableSeatList);
    }

    // Mock API용 메소드
    private List<ConcertSeatResponse> getAvailableSeatList(Long concertDetailId){
        List<ConcertSeatResponse> avaliableSeatList = List.of(
                new ConcertSeatResponse(1L,concertDetailId, "A1",ConcertSeatType.S,15000L),
                new ConcertSeatResponse(2L,concertDetailId, "A2",ConcertSeatType.R,10000L),
                new ConcertSeatResponse(3L,concertDetailId, "A3",ConcertSeatType.A,5000L),
                new ConcertSeatResponse(4L,concertDetailId, "A4",ConcertSeatType.A,5000L)
        );
        return avaliableSeatList;
    }

    private void validateToken(String queueToken) {
        if (queueToken == null || !waitingQueue.contains(queueToken)) {
            throw new IllegalArgumentException("유효하지 않은 접근입니다.");
        }
    }
}
