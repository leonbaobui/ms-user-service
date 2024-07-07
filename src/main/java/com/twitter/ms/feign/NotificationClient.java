package com.twitter.ms.feign;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import main.java.com.leon.baobui.configuration.FeignConfiguration;
import main.java.com.leon.baobui.dto.request.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static main.java.com.leon.baobui.constants.FeignConstants.NOTIFICATION_SERVICE;
import static main.java.com.leon.baobui.constants.PathConstants.API_V1_NOTIFICATION;

@FeignClient(value = NOTIFICATION_SERVICE, url = "${service.downstream-url.ms-notification-service}", path = API_V1_NOTIFICATION, configuration = FeignConfiguration.class)
public interface NotificationClient {

    @CircuitBreaker(name = NOTIFICATION_SERVICE)
    @PostMapping
    void sendNotification(@RequestBody NotificationRequest request);
}
