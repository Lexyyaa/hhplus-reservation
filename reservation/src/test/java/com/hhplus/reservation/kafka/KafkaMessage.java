package com.hhplus.reservation.kafka;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessage {
    private String id;
    private String message;
    @Override
    public String toString() {
        return "KafkaMessage{id='" + id + "', message='" + message + "'}";
    }
}
