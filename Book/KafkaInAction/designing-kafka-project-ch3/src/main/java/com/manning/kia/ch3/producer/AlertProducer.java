package com.manning.kia.ch3.producer;

import com.manning.kia.ch3.model.Alert;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Properties;

import static com.manning.kia.ch3.model.AlertStatus.Critical;

public class AlertProducer {
    private static final Logger log = LoggerFactory.getLogger(AlertProducer.class);

    public static void main(String[] args) {
        Properties kaProperties = new Properties();
        kaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        kaProperties.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        kaProperties.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        kaProperties.put("schema.registry.url", "http://localhost:8081");

        try(Producer<Long, Alert> producer = new KafkaProducer<>(kaProperties)) {
            Alert alert = new Alert(12345L, Instant.now().toEpochMilli(), Critical);

            log.info("kinaction_info Alert --> {}", alert);

            ProducerRecord<Long, Alert> producerRecord = new ProducerRecord<>("kinaction_alert", alert.getSensorId(), alert);
            producer.send(producerRecord);
        }
    }
}
