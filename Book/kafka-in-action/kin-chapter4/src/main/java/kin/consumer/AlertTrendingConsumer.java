package kin.consumer;

import kin.serde.AlertKeySerde;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class AlertTrendingConsumer {
    private static final Logger log = LoggerFactory.getLogger(AlertTrendingConsumer.class);

    private volatile boolean keepConsuming = true;

    public static void main(String[] args) {
        Properties kaProperties = new Properties();
        kaProperties.put("bootstrap.servers", "localhost:9092,localhost:9093");
        kaProperties.put("group.id", "alerttrendconsumer");
        kaProperties.put("enable.auto.commit", "true");
        kaProperties.put("auto.commit.interval.ms", "1000");
        kaProperties.put("key.deserializer", AlertKeySerde.class.getName());
        kaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        AlertTrendingConsumer alertTrendingConsumer = new AlertTrendingConsumer();
        alertTrendingConsumer.consume(kaProperties);
        Runtime.getRuntime().addShutdownHook(new Thread(alertTrendingConsumer::shutdown));
    }

    private void consume(Properties props) {
        try(KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList("kinaction_alerttrend"));

            while (keepConsuming) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record: consumerRecords) {
                    System.out.println("***** [Consumer Record] offset = " + record.offset() + ", key = " + record.key() + ", value = " + record.value());
                }
            }
        }
    }

    private void shutdown() {
        keepConsuming = false;
    }
}
