package com.hhplus.reservation.domain.message;

import com.hhplus.reservation.domain.common.Timestamped;
import com.hhplus.reservation.domain.payment.PaymentEvent;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "message_outbox",
    uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "event_type"})
)
public class MessageOutbox extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "domain_name")
    private String domainName;

    @Column(name = "topic", nullable = false)
    private String topic;

    @Column(name = "payload", columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column(name = "event_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageStatus eventStatus;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "retry_cnt")
    private Long retryCnt;

    public void published(){
        this.publishedAt = LocalDateTime.now();
        this.eventStatus = MessageStatus.PUBLISHED;
    }

    public void countUp(){
        this.retryCnt++;
    }
}


