package com.jd.adapter.in;

import com.jd.adapter.in.model.PointReviewEvent;
import com.jd.application.port.in.PointReviewUseCase;
import com.jd.util.JsonUtils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jaedoo Lee
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ExtensionMethod(JsonUtils.class)
public class PointExListener implements AcknowledgingMessageListener<String, Object> {

    private final PointReviewUseCase pointReviewUseCase;

    @SneakyThrows
    @Override
    @KafkaListener(topics = "point_review", groupId = "point_1")
    public void onMessage(ConsumerRecord<String, Object> consumerRecord, Acknowledgment acknowledgment) {
        PointReviewEvent event = consumerRecord.value().toString().toModel(PointReviewEvent.class);

        log.info("Consumed PointExListener, actionType: {}, reviewId : {}, userId : {}, placeId : {}", event.getAction(), event.getReviewId(), event.getUserId(), event.getPlaceId());
        acknowledgment.acknowledge();
    }
}
