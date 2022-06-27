package com.jd.adapter.out;

import com.jd.application.Topic;
import com.jd.application.port.out.MessageBroker;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jaedoo Lee
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer implements MessageBroker {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(Topic topic, String message) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic.getValue(), message);

        try {
            future.get(10, TimeUnit.SECONDS);
        } catch (Exception ex) {
            log.error("Unable to topic= {}, send message={} due to : {}", topic.name(), message, ex.getMessage());
        }
    }
}
