package com.jd.config;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Jaedoo Lee
 */
@Component
public class KafkaStreamsProcessor {

    final Serde<String> STRING_SERDE = Serdes.String();

    @Autowired
    void buildPipeline(StreamsBuilder streamsBuilder) {
        KStream<String, String> messageStream = streamsBuilder
            .stream("point-review", Consumed.with(STRING_SERDE, STRING_SERDE));
        messageStream.flatMapValues(value -> Arrays.asList(value.split(" ")))
                     .to("point-review-calculate");
    }

}
