package kin.chapter3;

import kin.chapter3.model.Alert;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Properties;

import static kin.chapter3.model.AlertStatus.Critical;

public class AlertProducer {
    private static final Logger log = LoggerFactory.getLogger(AlertProducer.class);

    public static void main(String[] args) {
        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        producerProperties.put("key.serializer", "org.apache.kafka.common.serialization.LongSerializer");
        producerProperties.put("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        producerProperties.put("schema.registry.url", "http://localhost:8081");

        try(Producer<Long, Alert> producer = new KafkaProducer<>(producerProperties)) {
            Alert alert = new Alert(12345L, Instant.now().toEpochMilli(), Critical);

            log.info("Alert -> {}", alert);

            ProducerRecord<Long, Alert> producerRecord = new ProducerRecord<>("alert-connect", alert.getSensorId(), alert);

            producer.send(producerRecord);
        }
    }
}
