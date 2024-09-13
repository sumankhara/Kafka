package com.manning.kia.ch4.producer;

import com.manning.kia.ch4.config.ProducerConfig;
import com.manning.kia.ch4.model.Alert;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class AlertTrendingProducer {
    private static final Logger log = LoggerFactory.getLogger(AlertTrendingProducer.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties = ProducerConfig.producerProperties();

        try(Producer<Alert, String> producer = new KafkaProducer<>(properties)) {
            Alert alert = new Alert(0, "Stage 0", "CRITICAL", "Stage 0 stopped");
            ProducerRecord<Alert, String> producerRecord = new ProducerRecord<>("kinaction_alert", alert, alert.getAlertMessage());

            RecordMetadata result = producer.send(producerRecord).get();

            log.info("kinaction_alert offset = {}, topic = {}, timestamp = {}, partition = {}",
                    result.offset(), result.topic(), result.timestamp(), result.partition());
        }
    }
}
