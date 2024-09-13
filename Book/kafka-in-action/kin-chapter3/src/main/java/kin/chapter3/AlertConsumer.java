package kin.chapter3;

import kin.chapter3.model.Alert;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class AlertConsumer {

    static final Logger log = LoggerFactory.getLogger(AlertConsumer.class);

    private volatile boolean keepConsuming = true;

    public static void main(String[] args) {
        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        producerProperties.put("group.id", "alertconsumer");
        producerProperties.put("enable.auto.commit", "true");
        producerProperties.put("auto.commit.interval.ms", "1000");
        producerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.LongDeserializer");
        producerProperties.put("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");
        producerProperties.put("schema.registry.url", "http://localhost:8081");

        AlertConsumer alertConsumer = new AlertConsumer();
        alertConsumer.consume(producerProperties);
        Runtime.getRuntime().addShutdownHook(new Thread(alertConsumer::shutdown));
    }

    private void consume(Properties props) {
        try(KafkaConsumer<Long, Alert> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList("alert-connect"));

            while (keepConsuming) {
                ConsumerRecords<Long, Alert> consumerRecords = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<Long, Alert> record: consumerRecords) {
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
