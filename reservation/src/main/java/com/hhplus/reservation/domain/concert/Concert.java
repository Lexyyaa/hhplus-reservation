package com.hhplus.reservation.domain.concert;

import com.hhplus.reservation.application.dto.ConcertInfo;
import com.hhplus.reservation.domain.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "concert")
public class Concert extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    public static ConcertInfo convert(Concert concert){
        return ConcertInfo.builder()
                .id(concert.getId())
                .title(concert.getTitle())
                .build();

    }
}