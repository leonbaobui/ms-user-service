package com.twitter.ms.producer;

import com.gmail.merikbest2015.constants.KafkaTopicConstants;
import com.gmail.merikbest2015.record.FollowUserEvent;
import com.twitter.ms.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import static com.gmail.merikbest2015.constants.PathConstants.AUTH_USER_ID_HEADER;

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
