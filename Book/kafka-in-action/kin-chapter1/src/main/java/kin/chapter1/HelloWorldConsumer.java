package kin.chapter1;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class HelloWorldConsumer {

    final static Logger log = LoggerFactory.getLogger(HelloWorldConsumer.class);

    private volatile boolean keepConsuming = true;

    public static void main(String[] args) {
        Properties producerProperties = new Properties();
        producerProperties.put("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        producerProperties.put("group.id", "helloconsumer");
        producerProperties.put("enable.auto.commit", "true");
        producerProperties.put("auto.commit.interval.ms", "1000");
        producerProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        producerProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        HelloWorldConsumer helloWorldConsumer = new HelloWorldConsumer();
        helloWorldConsumer.consume(producerProperties);
        Runtime.getRuntime().addShutdownHook(new Thread(helloWorldConsumer::shutdown));
    }

    private void consume(Properties props) {
        try(KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(Collections.singletonList("kinaction_helloworld"));

            while (keepConsuming) {
                ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, String> record: consumerRecords) {
                    System.out.println("[Consumer Record] offset = " + record.offset() + ", key = " + record.key() + ", value = " + record.value());
                }
            }
        }
    }

    private void shutdown() {
        keepConsuming = false;
    }
}
