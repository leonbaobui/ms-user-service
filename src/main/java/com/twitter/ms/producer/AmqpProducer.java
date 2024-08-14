package com.twitter.ms.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import main.java.com.leon.baobui.dto.request.EmailRequest;

@Component
@RequiredArgsConstructor
public class AmqpProducer {
    @Value("${rabbitmq.internal-mail.exchanges}")
    private String exchange;
    @Value("${rabbitmq.internal-mail.routing-keys}")
    private String routingKey;

    private final AmqpTemplate amqpTemplate;

    public void sendEmail(EmailRequest emailRequest) {
        amqpTemplate.convertAndSend(exchange, routingKey, emailRequest);
    }
}
