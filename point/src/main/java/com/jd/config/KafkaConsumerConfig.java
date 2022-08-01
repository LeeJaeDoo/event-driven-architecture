package com.jd.config;

import com.jd.application.PointException;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Jaedoo Lee
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerConfig {

    private final KafkaProperties kafkaProperties;

    private Map<String, Object> consumerProperties(){
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getConsumer().getBootstrapServers());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getKeyDeserializer());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, kafkaProperties.getConsumer().getValueDeserializer());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "point");
        return props;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<String, Object>(consumerProperties()));
        factory.setCommonErrorHandler(defaultErrorHandler());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    private DefaultErrorHandler defaultErrorHandler() {
        DefaultErrorHandler defaultErrorHandler =  new DefaultErrorHandler((consumerRecord, exception) -> {
            log.error("consumer error, {}", consumerRecord.value(), exception);
        }, new FixedBackOff(1000L, 3L));

        defaultErrorHandler.addNotRetryableExceptions(PointException.class);

        return defaultErrorHandler;
    }

    private RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        // 재시도시 1초 후에 재 시도하도록 backoff delay 시간을 설정한다.
        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(1000L);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);
        // 최대 재시도 횟수 설정
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(2);
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }

    /*private RecoveryCallback<Object> recoveryCallback(KafkaTemplate<String, String> kafkaTemplate) {
        return context -> {
            final var retryCount = context.getRetryCount();
            final var acknowledgment = (Acknowledgment) context.getAttribute("acknowledgment");
            final var record = (ConsumerRecord) context.getAttribute("record");
            final var topic = "dlt_" + record.topic();
            final var value = record.value().toString();
            try {
                log.warn("[Send to dead letter topic] {} - retryCount: {} - value: {}.", topic, retryCount, value);
                kafkaTemplate.send(topic, value);
            } catch (Exception e) {
                log.error("[Fail to dead letter topic]: {}" , topic, e);
            }
            if (Objects.nonNull(acknowledgment)) {
                acknowledgment.acknowledge();
            }
            return Optional.empty();
        };
    }*/

    /*@Bean
    public NewTopic deadLetterTopic(AppKafkaProperties properties) {
        // https://docs.spring.io/spring-kafka/docs/2.8.2/reference/html/#configuring-topics
        return TopicBuilder.name(ORDERS + properties.deadletter().suffix())
                           // Use only one partition for infrequently used Dead Letter Topic
                           .partitions(1)
                           // Use longer retention for Dead Letter Topic, allowing for more time to troubleshoot
                           .config(TopicConfig.RETENTION_MS_CONFIG, "" + properties.deadletter().retention().toMillis())
                           .build();
    }*/

}
