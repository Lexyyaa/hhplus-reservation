package com.hhplus.reservation.infra.message;

import com.hhplus.reservation.domain.concert.Concert;
import com.hhplus.reservation.domain.message.MessageOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JPAMessageOutboxRepository extends JpaRepository<MessageOutbox, Long> {

}
