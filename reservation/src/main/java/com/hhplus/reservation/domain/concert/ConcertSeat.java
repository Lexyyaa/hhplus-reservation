package com.hhplus.reservation.domain.concert;

import com.hhplus.reservation.application.dto.ConcertSeatInfo;
import com.hhplus.reservation.domain.common.Timestamped;
import com.hhplus.reservation.support.error.CustomException;
import com.hhplus.reservation.support.error.ErrorCode;
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
@Table(name = "concert_seat")
public class ConcertSeat extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "concert_schedule_id")
    private Long concertScheduleId;

    @Column(name = "seat_num")
    private String seatNum;

    @Column(name = "seat_type")
    @Enumerated(EnumType.STRING)
    private ConcertSeatType seatType;

    @Column(name = "seat_price")
    private Long seatPrice;

    @Column(name = "seat_status")
    @Enumerated(EnumType.STRING)
    private ConcertSeatStatus status;

    @Version
    @Column(name = "version")
    private Long version;

    public static List<ConcertSeatInfo> convert(List<ConcertSeat> seats){
        return seats.stream()
                .map(seat -> ConcertSeatInfo.builder()
                        .id(seat.getId())
                        .concertScheduleId(seat.getConcertScheduleId())
                        .seatNum(seat.getSeatNum())
                        .seatPrice(seat.getSeatPrice())
                        .status(seat.getStatus())
                        .build()
                )
                .collect(Collectors.toList());
    }

    public static void chkAllSeatAvaliable(int seatsSize,Long avaliableCnt){
        if(seatsSize != avaliableCnt){
            throw new CustomException(ErrorCode.UNAVAILABLE_SEAT);
        }
    }
}