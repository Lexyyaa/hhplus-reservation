package com.hhplus.reservation.application.usecase;

import com.hhplus.reservation.application.dto.ConcertInfo;
import com.hhplus.reservation.application.dto.ConcertScheduleInfo;
import com.hhplus.reservation.application.dto.PaymentInfo;
import com.hhplus.reservation.application.dto.ReservationInfo;
import com.hhplus.reservation.domain.concert.ConcertService;
import com.hhplus.reservation.domain.message.MessageOutbox;
import com.hhplus.reservation.domain.message.MessageOutboxService;
import com.hhplus.reservation.domain.message.MessageStatus;
import com.hhplus.reservation.domain.payment.*;
import com.hhplus.reservation.domain.point.UserPointService;
import com.hhplus.reservation.domain.queue.WaitingQueueService;
import com.hhplus.reservation.domain.reserve.ReservationService;
import com.hhplus.reservation.interfaces.dto.payment.PaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentUsecase {
    private final UserPointService userPointService;
    private final ReservationService reservationService;
    private final ConcertService concertService;
    private final PaymentService paymentService;
    private final PaymentEventPublisher paymentEventPublisher;

    @Transactional
    public PaymentResponse pay(String token, Long reservationId, Long userId){

        ReservationInfo reservation = reservationService.getReservation(reservationId);

        ConcertScheduleInfo schedule = concertService.getConcertSchedule(reservation.getConcertScheduleId());
        ConcertInfo concert = concertService.getConcert(schedule.getConcertId());

        reservationService.confirmedReservation(reservation);

        paymentService.validatePayment(reservation);
        Payment pay = paymentService.savePayment(userId,reservation);

        userPointService.payPoint(userId,reservation.getTotalPrice());

        paymentEventPublisher.deleteToken(new PaymentEvent.DeleteToken(token , pay.getId()));

        return PaymentInfo.convert(userId,concert.getTitle(),reservation.getTotalPrice());
    }
}






