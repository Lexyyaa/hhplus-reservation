package com.hhplus.reservation.infra.concert;

import com.hhplus.reservation.domain.concert.Concert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAConcertRepository extends JpaRepository<Concert, Long> {

}
