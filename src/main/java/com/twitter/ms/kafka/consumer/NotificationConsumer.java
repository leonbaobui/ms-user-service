package com.twitter.ms.kafka.consumer;


import lombok.RequiredArgsConstructor;

import com.twitter.ms.service.UserNotificationHandlerService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import main.java.com.leon.baobui.constants.KafkaTopicConstants;

@Component
@RequiredArgsConstructor
public class NotificationConsumer {
    private final UserNotificationHandlerService userNotificationHandlerService;

    @KafkaListener(topics = KafkaTopicConstants.RESET_USER_NOTIFICATIONS_COUNT_TOPIC, groupId = "${spring.kafka.consumer.group-id}")
    public void resetNotificationCountListener(Long notifiedUserEventId) {
        userNotificationHandlerService.resetNotificationCount(notifiedUserEventId);
    }
}
