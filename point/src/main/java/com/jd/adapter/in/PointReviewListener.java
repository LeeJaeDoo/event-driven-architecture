package com.jd.adapter.in;

import com.jd.adapter.in.model.PointReviewEvent;
import com.jd.application.port.in.PointReviewUseCase;
import com.jd.util.JsonUtils;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
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
public class PointReviewListener {

    private final PointReviewUseCase pointReviewUseCase;

    @SneakyThrows
    @KafkaListener(topics = "point_review", groupId = "point")
    public void onMessage(ConsumerRecord<String, String> record, Consumer<String, String> consumer) {
        PointReviewEvent event = record.value().toModel(PointReviewEvent.class);
        switch (event.getAction()) {
            case ADD:
                pointReviewUseCase.save(event);
                break;
            case MOD:
                pointReviewUseCase.modification(event);
                break;
            case DELETE:
                pointReviewUseCase.deduction(event);
                break;
        }
        consumer.commitSync();
        log.info("Consumed point_review message, actionType: {}, reviewId : {}, userId : {}, placeId : {}", event.getAction(), event.getReviewId(), event.getUserId(), event.getPlaceId());
    }

}
