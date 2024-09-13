package com.manning.kia.ch5.consumer;

import com.manning.kia.ch5.config.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AlertTrendingConsumer {

    private static final Logger log = LoggerFactory.getLogger(AlertTrendingConsumer.class);

    public static void main(String[] args) {
        Properties properties = ConsumerConfig.consumerProperties();

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        TopicPartition partitionTwo = new TopicPartition("kinaction_alert", 2);
        consumer.assign(List.of(partitionTwo));

        while(true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(250));

            for (ConsumerRecord<String, String> record: consumerRecords) {
                log.info("key = {}", record.key());
                log.info("message = {}", record.value());
                log.info("kinaction_alert offset = {}, topic = {}, timestamp = {}, partition = {}",
                        record.offset(), record.topic(), record.timestamp(), record.partition());

                commitOffset(record.offset(), record.partition(), record.topic(), consumer);
            }
        }
    }

    public static void commitOffset(long offset, int part, String topic, KafkaConsumer<String, String> consumer) {
        OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(++offset, "");

        Map<TopicPartition, OffsetAndMetadata> offsetMap = new HashMap<>();
        offsetMap.put(new TopicPartition(topic, part), offsetAndMetadata);

        consumer.commitSync(offsetMap);
    }
}
