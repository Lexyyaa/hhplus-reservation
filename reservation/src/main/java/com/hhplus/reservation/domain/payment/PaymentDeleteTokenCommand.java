package com.hhplus.reservation.domain.payment;

import lombok.*;
import org.springframework.context.ApplicationEvent;

@Getter
public class PaymentDeleteTokenCommand {
    private String token;

    public PaymentDeleteTokenCommand(String token){
        this.token = token;
    }
}