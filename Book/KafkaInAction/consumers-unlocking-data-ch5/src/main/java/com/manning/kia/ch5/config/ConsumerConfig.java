package com.manning.kia.ch5.config;

import com.manning.kia.ch5.serde.AlertKeyDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ConsumerConfig {

    public static Properties consumerProperties() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        properties.put("group.id", "kinaction_alert_consumer");
        properties.put("enable.auto.commit", "false");
        properties.put("key.deserializer", AlertKeyDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class);

        return properties;
    }
}
