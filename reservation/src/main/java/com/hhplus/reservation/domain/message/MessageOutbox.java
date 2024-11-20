package com.hhplus.reservation.domain.message;

import com.hhplus.reservation.domain.common.Timestamped;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "message_outbox")
public class MessageOutbox extends Timestamped {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "event_type")
    private String eventType; // 제목

    @Column(name = "domain_name")
    private String domainName;

    @Column(name = "topic")
    private String topic;

    @Column(name = "event_status")
    @Enumerated(EnumType.STRING)
    private MessageStatus eventStatus; // 발송상태 - PUBLISHING, PUBLISHED

    @Column(name = "event" , columnDefinition = "TEXT")
    private Object event; // 이벤트 내용
}


