package org.udemy.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.IntSummaryStatistics;
import java.util.Properties;
import java.util.stream.IntStream;

public class ProducerWithCallback {
    private static final Logger logger = LoggerFactory.getLogger(ProducerWithCallback.class.getSimpleName());

    public static void main(String[] args) {
        Properties properties = new Properties();

        properties.setProperty("bootstrap.servers", "localhost:9092,localhost:9093,localhost:9094");
        properties.setProperty("key.serializer", StringSerializer.class.getName());
        properties.setProperty("value.serializer", StringSerializer.class.getName());

        //properties.setProperty("partitioner.class", RoundRobinPartitioner.class.getName());

        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties);

        IntStream.rangeClosed(1, 10)
                .forEach(i -> {
                    ProducerRecord<String, String> producerRecord = new ProducerRecord<>("kafka_basics", "Hello World" + i);

                    kafkaProducer.send(producerRecord,
                            (metadata, exception) -> {
                                if (exception == null) {
                                    System.out.println("Received new metadata \n" +
                                            "Topic: " + metadata.topic() + "\n" +
                                            "Partition: " + metadata.partition() + "\n" +
                                            "Offset: " + metadata.offset() + "\n" +
                                            "Timestamp: " + metadata.timestamp());
                                } else {
                                    exception.printStackTrace();
                                }
                            });
                });

        //kafkaProducer.flush();

        kafkaProducer.close();
    }
}
