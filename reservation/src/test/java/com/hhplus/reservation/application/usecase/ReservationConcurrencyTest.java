package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.domain.reserve.ReservationRepository;
import com.hhplus.reservation.infra.reservation.JPAReservationRepository;
import com.hhplus.reservation.interfaces.dto.reserve.ReserveSeatRequest;
import com.hhplus.reservation.support.error.BizException;
import com.hhplus.reservation.support.error.ErrorType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ReservationConcurrencyTest {

    @Autowired
    private ReservationUsecase reservationUsecase;

    @Autowired
    private JPAReservationRepository reservationRepository;

}