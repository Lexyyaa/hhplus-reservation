package com.hhplus.reservation.domain.reserve;
import com.hhplus.reservation.domain.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reservation_seat")
public class ReservationSeat extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    public static List<ReservationSeat> createList(Long reservationId, List<Long> seatIds) {
        return seatIds.stream()
                .map(seatId -> ReservationSeat.builder()
                        .reservationId(reservationId)
                        .seatId(seatId)
                        .build())
                .collect(Collectors.toList());
    }
}
