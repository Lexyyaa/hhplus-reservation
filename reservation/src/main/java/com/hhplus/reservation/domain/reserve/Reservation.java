package com.hhplus.reservation.domain.reserve;

import com.hhplus.reservation.application.dto.ReservationInfo;
import com.hhplus.reservation.domain.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "concert_schedule_id", nullable = false)
    private Long concertScheduleId;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Column(name = "reserved", nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationType reserved;

    @Column(name = "reserve_request_at", nullable = false)
    private LocalDateTime reserveRequestAt;

    @Column(name = "reserve_expired_at", nullable = false)
    private LocalDateTime reserveExpiredAt;

    @Column(name = "reserved_at")
    private LocalDateTime reservedAt;

    @Transient
    List<ReservationSeat> seats;

    public static Reservation create(
            Long userId, Long concertScheduleId, Long totalPrice,ReservationType type) {
        return Reservation.builder()
                .userId(userId)
                .concertScheduleId(concertScheduleId)
                .totalPrice(totalPrice)
                .reserved(type)
                .reserveRequestAt(LocalDateTime.now())
                .reserveExpiredAt(LocalDateTime.now().plusMinutes(5))
                .build();
    }

    public static Reservation create(ReservationInfo reservation) {
        return Reservation.builder()
                .userId(reservation.getUserId())
                .concertScheduleId(reservation.getConcertScheduleId())
                .totalPrice(reservation.getTotalPrice())
                .reserved(reservation.getReserved())
                .reserveRequestAt(reservation.getReserveRequestAt())
                .reserveExpiredAt(reservation.getReserveExpiredAt())
                .build();
    }

    public static ReservationInfo convert(Reservation reservation){
        return ReservationInfo.builder()
                .id(reservation.getId())
                .userId(reservation.getUserId())
                .concertScheduleId(reservation.getConcertScheduleId())
                .totalPrice(reservation.getTotalPrice())
                .reserved(reservation.getReserved())
                .reserveRequestAt(reservation.getReserveRequestAt())
                .reserveExpiredAt(reservation.getReserveExpiredAt())
                .build();
    }
}