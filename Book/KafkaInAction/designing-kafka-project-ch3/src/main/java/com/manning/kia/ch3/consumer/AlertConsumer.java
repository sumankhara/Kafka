package com.manning.kia.ch3.consumer;

import com.manning.kia.ch3.model.Alert;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class AlertConsumer {
    private static final Logger log = LoggerFactory.getLogger(AlertConsumer.class);

    private volatile boolean keepConsuming = true;

    public static void main(String[] args) {
        Properties kaProperties = new Properties();
        kaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        kaProperties.put("group.id", "alertconsumer");
        kaProperties.put("enable.auto.commit", "true");
        kaProperties.put("auto.commit.interval.ms", "1000");
        kaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        kaProperties.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        kaProperties.put("schema.registry.url", "http://localhost:8081");

        AlertConsumer alertConsumer = new AlertConsumer();
        alertConsumer.consume(kaProperties);

        Runtime.getRuntime().addShutdownHook(new Thread(alertConsumer::shutdown));
    }

    private void consume(Properties properties) {
        try(KafkaConsumer<Long, Alert> consumer = new KafkaConsumer<>(properties)) {
            consumer.subscribe(List.of("kinaction_alert"));

            while (keepConsuming) {
                ConsumerRecords<Long, Alert> records = consumer.poll(Duration.ofMillis(200));
                for(ConsumerRecord<Long, Alert> record: records) {
                    System.out.println("[Consumer Record] offset = " + record.offset() + ", key = " + record.key() + ", value = " + record.value());
                    log.info("[Consumer Record] offset = {}, key = {}, value = {}",
                            record.offset(),
                            record.key(),
                            record.value());
                }
            }
        }
    }

    private void shutdown() {
        keepConsuming = false;
    }
}
