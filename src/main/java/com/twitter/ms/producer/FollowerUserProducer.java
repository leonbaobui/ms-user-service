package com.twitter.ms.producer;

import lombok.RequiredArgsConstructor;
import com.twitter.ms.model.User;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import main.java.com.leon.baobui.constants.KafkaTopicConstants;
import main.java.com.leon.baobui.record.FollowUserEvent;

import static main.java.com.leon.baobui.constants.PathConstants.AUTH_USER_ID_HEADER;

@Component
@RequiredArgsConstructor
public class FollowerUserProducer {
    private final KafkaTemplate<String, FollowUserEvent> kafkaTemplate;

    public void sendFollowUserEvent(User user, Long authUserId, boolean hasUserFollowed) {
        ProducerRecord<String, FollowUserEvent> producerRecord =
                new ProducerRecord<>(KafkaTopicConstants.FOLLOW_USER_TOPIC,
                        buildFollowUserRecord(user, authUserId, hasUserFollowed));
        producerRecord.headers().add(AUTH_USER_ID_HEADER, authUserId.toString().getBytes());
        kafkaTemplate.send(producerRecord);
    }

    private FollowUserEvent buildFollowUserRecord(User user, Long authUserId, boolean hasUserFollowed) {
        return FollowUserEvent.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .about(user.getAbout())
                .avatar(user.getAvatar())
                .privateProfile(user.isPrivateProfile())
                .active(user.isActive())
                .userFollow(hasUserFollowed)
                .build();
    }
}
