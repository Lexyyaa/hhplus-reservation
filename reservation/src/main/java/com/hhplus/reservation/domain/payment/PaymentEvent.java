package com.hhplus.reservation.domain.payment;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Builder
public class PaymentEvent {

    public enum EventType {
        DELETE_TOKEN
    }

    @Slf4j
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteToken {
        private String token;
        private Long messageKey;
        private EventType eventType;

        public static DeleteToken convertToEvent(String payload)  {
            DeleteToken event = null;
            try{
                event = new ObjectMapper().readValue(payload, DeleteToken.class);
            }catch (JsonProcessingException e) {
               throw new RuntimeException(e);
            }
            return event;
        }

        public DeleteToken(String token, Long messageKey) {
            this.token = token;
            this.messageKey = messageKey;
            this.eventType = EventType.DELETE_TOKEN;
        }
    }
}
