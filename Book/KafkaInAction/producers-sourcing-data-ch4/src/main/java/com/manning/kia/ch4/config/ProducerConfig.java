package com.manning.kia.ch4.config;

import com.manning.kia.ch4.producer.AlertTrendingPartitioner;
import com.manning.kia.ch4.serde.AlertKeySerde;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerConfig {
    public static Properties producerProperties() {
        Properties kaProperties = new Properties();

        kaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        kaProperties.put("acks", "all");
        kaProperties.put("retries", 3);
        kaProperties.put("max.in.flight.requests.per.connection", 1);

        kaProperties.put("key.serializer", AlertKeySerde.class.getName());
        kaProperties.put("value.serializer", StringSerializer.class);

        kaProperties.put("partitioner.class", AlertTrendingPartitioner.class.getName());

        return kaProperties;
    }
}
